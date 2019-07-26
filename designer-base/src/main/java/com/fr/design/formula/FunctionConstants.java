package com.fr.design.formula;

import com.fr.file.FunctionConfig;
import com.fr.function.AVERAGE;
import com.fr.function.CHAR;
import com.fr.function.COUNT;
import com.fr.function.DATE;
import com.fr.function.MAX;
import com.fr.function.MIN;
import com.fr.function.RANGE;
import com.fr.function.SUM;
import com.fr.function.TIME;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.EncodeConstants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.FunctionDefContainer;
import com.fr.stable.fun.mark.Mutable;
import com.fr.stable.script.Function;
import com.fr.stable.script.FunctionDef;

import javax.swing.DefaultListModel;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FunctionConstants {

	public static FunctionGroup PLUGIN = getPluginFunctionGroup();
	public static FunctionGroup CUSTOM = getCustomFunctionGroup();
	public static NameAndFunctionList COMMON = getCommonFunctionList();
	public static NameAndTypeAndFunctionList[] EMBFUNCTIONS = getEmbededFunctionListArray();
	public static FunctionGroup ALL = getAllFunctionGroup();

	static {
		loadEmbededFunctions();
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private FunctionConstants() {}

	private static void loadEmbededFunctions() {
		String pkgName = "com.fr.function";
		Class<Function> iface = Function.class;
		ClassLoader classloader = iface.getClassLoader();
		Enumeration<URL> urlEnumeration = null;
		try {
			urlEnumeration = classloader.getResources(pkgName.replace('.', '/'));
		} catch (IOException e) {
			FineLoggerFactory.getLogger().error(e.getMessage());
			return;
		}
		while (urlEnumeration.hasMoreElements()) {
			URL url = urlEnumeration.nextElement();
			String classFilePath = url.getFile();
			/*
			 * alex:url.getFile获取的地址中,如果有空格或中文会被URLEncoder.encode处理
			 * 会变成%20这种%打头的东西,但是new File的时候%20是无法解析成空格,所以在此需要做URLDecoder.decode处理
			 */
			try {
				classFilePath = URLDecoder.decode(classFilePath, EncodeConstants.ENCODING_UTF_8);
			} catch (UnsupportedEncodingException e1) {
                FineLoggerFactory.getLogger().error(e1.getMessage(), e1);
			}
            FineLoggerFactory.getLogger().info("ClassFilePath:" + classFilePath);
			if (isCustomFormulaPath(classFilePath)) {
				continue;
			}
			for (String fileName : findClassNamesUnderFilePath(classFilePath)) {
				try {
					Class<?> cls = Class.forName(pkgName + "." + fileName.substring(0, fileName.length() - 6));
					if (StableUtils.classInstanceOf(cls, iface)) {
						Function inst;
						inst = (Function)cls.newInstance();
						for (NameAndTypeAndFunctionList EMBFUNCTION : EMBFUNCTIONS) {
							if (EMBFUNCTION.test(inst)) {
								break;
							}
						}
					}
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignore) {
				} catch (Throwable e) {
					// 不要因为个别公式加载失败，而导致整个函数面板无法启动
					FineLoggerFactory.getLogger().error(e.getMessage());
				}
			}
		}
	}

	private static boolean isCustomFormulaPath(String classFilePath) {
		return !isJarPath(classFilePath) && isNotDebugMode();
	}

    private static boolean isNotDebugMode() {
        return GeneralUtils.readBuildNO().contains("-");
    }

    private static boolean isJarPath(String classFilePath) {
        return classFilePath.contains("!/");
    }

    /**
	 * 将函数分组插件中的函数添加到对应的列表中
	 * @param listModel
	 */
	public static void addFunctionGroupFromPlugins(DefaultListModel listModel){
		//hugh:自定义函数分组
        Set<Mutable> containers = ExtraClassManager.getInstance().getArray(FunctionDefContainer.MARK_STRING);
        if(!containers.isEmpty()){
        	for(Mutable container : containers){
        		listModel.addElement(createFunctionGroup((FunctionDefContainer)container));
        	}
        }
	}
	
	/**
	 * 创建一个新的分组
	 * @param container
	 * @return
	 */
	private static FunctionGroup createFunctionGroup(final FunctionDefContainer container){
		return new FunctionGroup() {
	        @Override
	        public String getGroupName() {
	            return container.getGroupName();
	        }

	        @Override
	        public NameAndDescription[] getDescriptions() {
	            FunctionDef[] fs = container.getFunctionDefs();
	            int count = fs.length;
	            FunctionDefNAD[] nads = new FunctionDefNAD[count];
	            for (int i = 0; i < count; i ++) {
	                nads[i] = new FunctionDefNAD(fs[i]);
	            }
	            return nads;
	        }
	    };
	}

	
	private static String[] findClassNamesUnderFilePath(String filePath) {
		java.util.List<String> classNameList = new ArrayList<String>();
		/*
		 * alex:如果是jar包中的class文件
		 * file:/D:/opt/FineReport6.5/WebReport/WEB-INF/lib/fr-server-6.5.jar!/com/fr/rpt/script/function
		 */
		if (isJarPath(filePath)) {
			String[] arr = filePath.split("!/");
			String jarPath = arr[0].substring(6); // alex:substring(6)去掉前面的file:/这六个字符
			String classPath = arr[1];
			if(classPath.endsWith("/")){
				classPath = classPath.substring(0, classPath.length() - 1);
			}
            if (!OperatingSystem.isWindows()){
                //windows里substring后是d:\123\456, mac下substring后是Application/123/456
                jarPath = StringUtils.perfectStart(jarPath, "/");
            }
			
			ZipFile zip = null;
			try {
				zip = new ZipFile(jarPath);
				Enumeration entries = zip.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					if (entry.isDirectory()) {
						continue;
					}
					
					String entryName = entry.getName();
					if (!entryName.contains(classPath) || !entryName.endsWith(".class")) {
						continue;
					}
					
					classNameList.add(entryName.substring(classPath.length() + 1));
				}
			} catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
			} finally {
				if(null != zip){
					try {
						zip.close();
					} catch (IOException e) {
						FineLoggerFactory.getLogger().error(e.getMessage(), e);
					}
				}
			}
		} else {
			File dir = new File(filePath);
			File[] files = dir.listFiles();
			if (files != null) {
				for (File f : files) {
					String fileName = f.getName();
					if (fileName.endsWith(".class")) {
						classNameList.add(fileName);
					}
				}
			}
		}
		
		return classNameList.toArray(new String[0]);
	}

	private static FunctionGroup getPluginFunctionGroup() {
		return new FunctionGroup() {
			@Override
			public String getGroupName() {
				return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Base_Formula_Plugin");
			}

			@Override
			public NameAndDescription[] getDescriptions() {
				FunctionDef[] fs = ExtraClassManager.getInstance().getFunctionDef();
				int count = fs.length;
				FunctionDefNAD[] nads = new FunctionDefNAD[count];
				for (int i = 0; i < count; i ++) {
					nads[i] = new FunctionDefNAD(fs[i]);
				}
				return nads;
			}
		};
	}

	private static FunctionGroup getCustomFunctionGroup() {
		return new FunctionGroup() {
			@Override
			public String getGroupName() {
				return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Custom_Function");
			}

			@Override
			public NameAndDescription[] getDescriptions() {
				FunctionConfig funtionManager = FunctionConfig.getInstance();
				if (funtionManager != null) {
					int functionDefCount = funtionManager.getFunctionDefCount();

					FunctionDefNAD[] nads = new FunctionDefNAD[functionDefCount];
					for (int i = 0; i < functionDefCount; i++) {
						nads[i] = new FunctionDefNAD(funtionManager.getFunctionDef(i));
					}

					return nads;
				}

				return new NameAndDescription[0];
			}
		};
	}

	private static NameAndFunctionList getCommonFunctionList() {
		return new NameAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Most_Recently_Used"), new Function[] {
				new SUM(), new COUNT(), new AVERAGE(), new CHAR(), new DATE(), new MAX(), new MIN(), new TIME(), new RANGE()
		});
	}

	private static NameAndTypeAndFunctionList[] getEmbededFunctionListArray() {
		return new NameAndTypeAndFunctionList[] {
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Math_&_Trig"), Function.MATH),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Text"), Function.TEXT),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Date_&_Time"), Function.DATETIME),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Logical"), Function.LOGIC),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Array"), Function.ARRAY),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Report"), Function.REPORT),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Other"), Function.OTHER),
				new NameAndTypeAndFunctionList(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_Type_Hierarchy"), Function.HA)
		};
	}

	private static FunctionGroup getAllFunctionGroup() {
		return new FunctionGroup() {
			@Override
			public String getGroupName() {
				return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_All");
			}

			@Override
			public NameAndDescription[] getDescriptions() {
				List<NameAndDescription> all = new ArrayList<NameAndDescription>();
				for (int i = 0; i < EMBFUNCTIONS.length; i++) {
					all.addAll(Arrays.asList(EMBFUNCTIONS[i].getDescriptions()));
				}
				Collections.addAll(all, PLUGIN.getDescriptions());
				Collections.addAll(all, CUSTOM.getDescriptions());
				//hugh:自定义函数分组
				Set<Mutable> containers = ExtraClassManager.getInstance().getArray(FunctionDefContainer.MARK_STRING);
				if(!containers.isEmpty()){
					for(Mutable container : containers){
						Collections.addAll(all,createFunctionGroup(((FunctionDefContainer)container)).getDescriptions());
					}
				}

				Collections.sort(all, new Comparator<NameAndDescription>() {
					@Override
					public int compare(NameAndDescription o1, NameAndDescription o2) {
						return ComparatorUtils.compare(o1.getName(), o2.getName());
					}
				});

				return all.toArray(new NameAndDescription[0]);
			}
		};
	}
}
