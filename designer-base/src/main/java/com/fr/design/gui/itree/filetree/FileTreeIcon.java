package com.fr.design.gui.itree.filetree;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.icon.LockIcon;
import com.fr.design.mainframe.NewTemplateFileProvider;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Set;

public class FileTreeIcon {
    private FileTreeIcon() {
    }

    public static final Icon BLANK_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/blank.gif");

    public static final Icon FOLDER_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/folder.png");
    public static final Icon FOLDER_HALF_IMAGE_ICON =
            BaseUtils.readIcon("/com/fr/design/images/gui/filetree_folder_half_authority_normal.png");

    public static final Icon FILE_IMAGE_ICON = UIManager.getIcon("FileView.fileIcon");

    public static final Icon JAVA_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/javaFile.gif");
    public static final Icon CLASS_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/classFile.gif");
    public static final Icon JSP_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/jspFile.gif");
    public static final Icon JS_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/jsFile.gif");
    public static final Icon XML_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/xmlFile.gif");
    public static final Icon HTML_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/htmlFile.gif");
    public static final Icon JAR_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/jarFile.gif");
    public static final Icon GIF_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/gifFile.gif");
    public static final Icon JPG_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/jpgFile.gif");
    public static final Icon BMP_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/bmpFile.gif");
    public static final Icon CPT_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/cptFile.png");
    public static final Icon FRM_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/frm.png");
    public static final Icon CHT_FILE_IMAGE_ICON = BaseUtils.readIcon("/com/fr/design/images/gui/cht.png");

    public static final Icon MODERN_CPT_FILE_IMAGE_ICON =
            BaseUtils.readIcon("/com/fr/design/images/gui/modern_style_cpt_file_icon_16x16.png");
    public static final Icon MODERN_FRM_FILE_IMAGE_ICON =
            BaseUtils.readIcon("/com/fr/design/images/gui/modern_style_frm_file_icon_16x16.png");
    public static final Icon MODERN_CHT_FILE_IMAGE_ICON =
            BaseUtils.readIcon("/com/fr/design/images/gui/modern_style_cht_file_icon_16x16.png");

    public static final LockIcon FOLDER_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/fold.png"));
    public static final LockIcon FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/file.gif"));

    public static final LockIcon JAVA_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/javaFile.gif"));
    public static final LockIcon CLASS_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/classFile.gif"));
    public static final LockIcon JSP_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/jspFile.gif"));
    public static final LockIcon ANT_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/antFile.gif"));
    public static final LockIcon JS_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/jsFile.gif"));
    public static final LockIcon XML_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/xmlFile.gif"));
    public static final LockIcon HTML_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/htmlFile.gif"));
    public static final LockIcon JAR_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/jarFile.gif"));
    public static final LockIcon GIF_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/gifFile.gif"));
    public static final LockIcon JPG_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/jpgFile.gif"));
    public static final LockIcon BMP_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/bmpFile.gif"));
    public static final LockIcon CPT_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/base/images/oem/cptlocked.png"));
    public static final LockIcon FRM_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/frmlocked.png"));
    public static final LockIcon CHT_FILE_LOCK_ICON =
            new LockIcon(BaseUtils.readImage("/com/fr/design/images/gui/frmlocked.png"));
    /**
     * file types
     */
    public static final String[] DEFAULT_ANT_FILE_TYPES = new String[]{"build.xml"};
    public static final String[] DEFAULT_JAVA_FILE_TYPES = new String[]{".java"};
    public static final String[] DEFAULT_CLASS_FILE_TYPES = new String[]{".class"};
    public static final String[] DEFAULT_JSP_FILE_TYPES = new String[]{".jsp", ".jspf", ".jsf"};
    public static final String[] DEFAULT_HTML_FILE_TYPES = new String[]{".html", ".htm"};
    public static final String[] DEFAULT_JS_FILE_TYPES = new String[]{".js"};
    public static final String[] DEFAULT_XML_FILE_TYPES = new String[]{".xml", ".xsl"};
    public static final String[] DEFAULT_TEXT_FILE_TYPES = new String[]{
            ".properties", ".txt", ".cmd", ".bat", ".sh"};
    //file types.
    public final static int JAVA_FILE = 0; //.java
    public final static int JSP_FILE = 1; //.jsp, .jspf, .jsf
    public final static int HTML_FILE = 2; //.html, .htm
    public final static int JS_FILE = 3; //.js
    public final static int XML_FILE = 4; //.xml, .xsl, .dtd, .tld
    public final static int TEXT_FILE = 5; //.properties, .txt, cmd, bat, sh
    public final static int CLASS_FILE = 6; //.class
    public final static int ZIP_FILE = 7; //.jar, .zip, .war, .ear
    public final static int GIF_FILE = 8; //.gif
    public final static int JPG_FILE = 9; //.jpg
    public final static int BMP_FILE = 10; //.bmp
    public final static int CPT_FILE = 11; //.cpt
    public final static int FRM_FILE = 12; //.form .frm
    public final static int CHT_FILE = 13; //.chart .cht

    public static Icon getIcon(File file) {
        return FileTreeIcon.getIcon(file, false);
    }

    public static Icon getIcon(File file, boolean isLocked) {
        if (file.isDirectory()) {
            if (isLocked) {
                return FOLDER_LOCK_ICON;
            } else {
                return FOLDER_IMAGE_ICON;
            }
        }

        return FileTreeIcon.getIcon(FileTreeIcon.getFileType(file.getName()), isLocked);
    }

    public static Icon getFolderHalfImageIcon() {
        return FOLDER_HALF_IMAGE_ICON;
    }

    /**
     * 获取文件节点对应的图标
     *
     * @param node 文件节点
     * @return 文件节点的图标
     */
    public static Icon getIcon(FileNode node) {
        // 如果文件节点锁不为空且不是当前用户锁定的该模板，那么模板ICON 提示用户当前文件节点被锁
        boolean showLock = node.getLock() != null && !ComparatorUtils.equals(node.getUserID(), node.getLock());
        return getIcon(node, showLock);
    }

    public static Icon getIcon(FileNode node, boolean isShowLock) {
        String path = StableUtils.pathJoin(WorkContext.getCurrent().getPath(), node.getEnvPath());
        if (WorkContext.getCurrent().isLocal()) {
            File ff = new File(path);
            if (ff.exists()) {
                if (node.isDirectory()) {
                    return FileTreeIcon.FOLDER_IMAGE_ICON;
                }
                return getLocalFileIcon(path);
            }
        }
        if (node.isDirectory()) {
            return FileTreeIcon.FOLDER_IMAGE_ICON;
        } else {
            return getRemoteFileIcon(node, isShowLock);
        }
    }

    private static Icon getLocalFileIcon(String path) {
        Set<NewTemplateFileProvider> providers = ExtraDesignClassManager.getInstance().getArray(NewTemplateFileProvider.XML_TAG);
        for (NewTemplateFileProvider provider : providers) {
            if (provider.getLocalFileIcon(path) != null) {
                return provider.getLocalFileIcon(path);
            }
        }
        return FileSystemView.getFileSystemView().getSystemIcon(new File(path));
    }

    private static Icon getRemoteFileIcon(FileNode node, boolean isShowLock){
        Set<NewTemplateFileProvider> providers = ExtraDesignClassManager.getInstance().getArray(NewTemplateFileProvider.XML_TAG);
        for (NewTemplateFileProvider provider : providers) {
            if (provider.getRemoteFileIcon(node, isShowLock) != null) {
                return provider.getRemoteFileIcon(node, isShowLock);
            }
        }
        return FileTreeIcon.getIcon(FileTreeIcon.getFileType(node.getName()), isShowLock);
    }

    private static Icon getIcon(int fileType, boolean isLocked) {
        if (fileType == JAVA_FILE) {
            if (isLocked) {
                return FileTreeIcon.JAVA_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.JAVA_FILE_IMAGE_ICON;
            }
        } else if (fileType == CLASS_FILE) {
            if (isLocked) {
                return FileTreeIcon.CLASS_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.CLASS_FILE_IMAGE_ICON;
            }
        } else if (fileType == JSP_FILE) {
            if (isLocked) {
                return FileTreeIcon.JSP_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.JSP_FILE_IMAGE_ICON;
            }
        } else if (fileType == XML_FILE) {
            if (isLocked) {
                return FileTreeIcon.XML_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.XML_FILE_IMAGE_ICON;
            }
        } else if (fileType == HTML_FILE) {
            if (isLocked) {
                return FileTreeIcon.HTML_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.HTML_FILE_IMAGE_ICON;
            }
        } else if (fileType == JS_FILE) {
            if (isLocked) {
                return FileTreeIcon.JS_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.JS_FILE_IMAGE_ICON;
            }
        } else if (fileType == ZIP_FILE) {
            if (isLocked) {
                return FileTreeIcon.JAR_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.JAR_FILE_IMAGE_ICON;
            }
        } else if (fileType == GIF_FILE) { //gif
            if (isLocked) {
                return FileTreeIcon.GIF_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.GIF_FILE_IMAGE_ICON;
            }
        } else if (fileType == JPG_FILE) { //jpg
            if (isLocked) {
                return FileTreeIcon.JPG_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.JPG_FILE_IMAGE_ICON;
            }
        } else if (fileType == BMP_FILE) { //bmp
            if (isLocked) {
                return FileTreeIcon.BMP_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.BMP_FILE_IMAGE_ICON;
            }
        } else if (fileType == CPT_FILE) { //cpt
            if (isLocked) {
                return FileTreeIcon.CPT_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.MODERN_CPT_FILE_IMAGE_ICON;
            }
        } else if (fileType == FRM_FILE) { //form frm
            if (isLocked) {
                return FileTreeIcon.FRM_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.MODERN_FRM_FILE_IMAGE_ICON;
            }
        } else if (fileType == CHT_FILE) { //chart cht
            if (isLocked) {
                return FileTreeIcon.CHT_FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.MODERN_CHT_FILE_IMAGE_ICON;
            }
        } else {
            if (isLocked) {
                return FileTreeIcon.FILE_LOCK_ICON;
            } else {
                return FileTreeIcon.FILE_IMAGE_ICON;
            }
        }
    }

    /**
     * Gets file type according to file name.
     */
    public static int getFileType(String fileName) {
        fileName = fileName.toLowerCase(); //to lower case in the first.
        for (int i = 0; i < DEFAULT_JAVA_FILE_TYPES.length; i++) {
            if (fileName.endsWith(DEFAULT_JAVA_FILE_TYPES[i])) {
                return JAVA_FILE;
            }
        }
        for (int i = 0; i < DEFAULT_JSP_FILE_TYPES.length; i++) {
            if (fileName.endsWith(DEFAULT_JSP_FILE_TYPES[i])) {
                return JSP_FILE;
            }
        }
        for (int i = 0; i < DEFAULT_HTML_FILE_TYPES.length; i++) {
            if (fileName.endsWith(DEFAULT_HTML_FILE_TYPES[i])) {
                return HTML_FILE;
            }
        }
        for (int i = 0; i < DEFAULT_XML_FILE_TYPES.length; i++) {
            if (fileName.endsWith(DEFAULT_XML_FILE_TYPES[i])) {
                return XML_FILE;
            }
        }
        if (fileName.endsWith(".class")) {
            return CLASS_FILE;
        } else if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
            return ZIP_FILE;
        } else if (fileName.endsWith(".ear") || fileName.endsWith(".war")) {
            return ZIP_FILE;
        } else if (fileName.endsWith(".gif")) {
            return GIF_FILE;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpe")) {
            return JPG_FILE;
        } else if (fileName.endsWith(".jpeg")) {
            return JPG_FILE;
        } else if (fileName.endsWith(".bmp")) {
            return BMP_FILE;
        } else if (fileName.endsWith(".cpt")) {
            return CPT_FILE;
        } else if (fileName.endsWith(".frm") || fileName.endsWith(".form")) {
            return FRM_FILE;
        } else if (fileName.endsWith(".cht") || fileName.endsWith(".chart")) {
            return CHT_FILE;
        } else {
            return TEXT_FILE;
        }
    }
}
