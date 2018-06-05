package com.fr.start.module;

import com.fr.base.ModifiedTable;
import com.fr.base.Parameter;
import com.fr.base.StoreProcedureParameter;
import com.fr.base.TableData;
import com.fr.base.env.serializer.OldSerializerAdapter;
import com.fr.base.env.serializer.ProcedureDataModelSerializer;
import com.fr.base.env.user.RemoteUserCenter;
import com.fr.base.operator.connection.DataConnectionOperator;
import com.fr.base.operator.connection.LocalDataConnectionOperator;
import com.fr.base.operator.file.FileOperator;
import com.fr.base.operator.file.LocalFileOperator;
import com.fr.core.env.operator.envinfo.EnvInfoOperator;
import com.fr.core.env.operator.envinfo.LocalEnvInfoOperator;
import com.fr.core.env.operator.user.UserCenter;
import com.fr.core.env.proxy.EnvProxy;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.impl.Connection;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.dav.DavXMLUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.module.Activator;
import com.fr.start.EnvSwitcher;
import com.fr.start.StartServer;
import com.fr.startup.opeartors.LocalUserCenter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerEnvProvider extends Activator {

    @Override
    public void start() {

        String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                    StartServer.browserDemoURL();
                    break;
                }
            }
        }
        getRoot().setSingleton(EnvSwitcher.class, new EnvSwitcher());

        initEnvOperators();

        //设置好环境即可，具体跟环境有关的模块会自动调用
        getRoot().getSingleton(EnvSwitcher.class).switch2LastEnv();
    }

    @Override
    public void stop() {
        //清空模块
        getRoot().removeSingleton(EnvSwitcher.class);
    }

    private void initEnvOperators() {
        addSerializers();
        EnvProxy.addLocalService(FileOperator.class, new LocalFileOperator());
        EnvProxy.addLocalService(DataConnectionOperator.class, new LocalDataConnectionOperator());
        EnvProxy.addLocalService(EnvInfoOperator.class, new LocalEnvInfoOperator());
        EnvProxy.addLocalService(UserCenter.class, new LocalUserCenter());
        EnvProxy.addRemoteService(UserCenter.class, new RemoteUserCenter());
    }

    private void addSerializers() {
        EnvProxy.addSerializer(ProcedureDataModel[].class, new ProcedureDataModelSerializer());

        EnvProxy.addSerializer(ModifiedTable.class, new OldSerializerAdapter<ModifiedTable>(
                new OldSerializerAdapter.OldSerializer<ModifiedTable>() {
                    @Override
                    public void serializer(ModifiedTable obj, OutputStream out) throws Exception {
                        DavXMLUtils.writeXMLModifiedTables(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<ModifiedTable>() {
                    @Override
                    public ModifiedTable desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLModifiedTables(in);
                    }
                }
        ));

        EnvProxy.addSerializer(com.fr.data.impl.Connection.class, new OldSerializerAdapter<com.fr.data.impl.Connection>(
                new OldSerializerAdapter.OldSerializer<Connection>() {
                    @Override
                    public void serializer(Connection obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileDatabaseConnection(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<Connection>() {
                    @Override
                    public Connection desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLDatabaseConnection(in);
                    }
                }
        ));

        EnvProxy.addSerializer(FileNode[].class, new OldSerializerAdapter<FileNode[]>(
                new OldSerializerAdapter.OldSerializer<FileNode[]>() {
                    @Override
                    public void serializer(FileNode[] obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileNodes(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<FileNode[]>() {
                    @Override
                    public FileNode[] desializer(InputStream in) {
                        return DavXMLUtils.readXMLFileNodes(in);
                    }
                }
        ));

        EnvProxy.addSerializer(TableProcedure[].class, new OldSerializerAdapter<TableProcedure[]>(
                new OldSerializerAdapter.OldSerializer<TableProcedure[]>() {
                    @Override
                    public void serializer(TableProcedure[] obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileSQLTable(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<TableProcedure[]>() {
                    @Override
                    public TableProcedure[] desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLSQLTables(in);
                    }
                }
        ));

        EnvProxy.addSerializer(TableData.class, new OldSerializerAdapter<TableData>(
                new OldSerializerAdapter.OldSerializer<TableData>() {
                    @Override
                    public void serializer(TableData obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileTableData(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<TableData>() {
                    @Override
                    public TableData desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLTableData(in);
                    }
                }
        ));

        EnvProxy.addSerializer(Parameter[].class, new OldSerializerAdapter<Parameter[]>(
                new OldSerializerAdapter.OldSerializer<Parameter[]>() {
                    @Override
                    public void serializer(Parameter[] obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileParameters(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<Parameter[]>() {
                    @Override
                    public Parameter[] desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLParameters(in);
                    }
                }
        ));

        EnvProxy.addSerializer(StoreProcedure.class, new OldSerializerAdapter<StoreProcedure>(
                new OldSerializerAdapter.OldSerializer<StoreProcedure>() {
                    @Override
                    public void serializer(StoreProcedure obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileStoreProcedure(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<StoreProcedure>() {
                    @Override
                    public StoreProcedure desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLStoreProcedure(in);
                    }
                }
        ));

        EnvProxy.addSerializer(StoreProcedureParameter[].class, new OldSerializerAdapter<StoreProcedureParameter[]>(
                new OldSerializerAdapter.OldSerializer<StoreProcedureParameter[]>() {
                    @Override
                    public void serializer(StoreProcedureParameter[] obj, OutputStream out) {
                        DavXMLUtils.writeXMLFileParameters(obj, out);
                    }
                },
                new OldSerializerAdapter.OldDeserializer<StoreProcedureParameter[]>() {
                    @Override
                    public StoreProcedureParameter[] desializer(InputStream in) throws Exception {
                        return DavXMLUtils.readXMLStoreProcedureParameters(in);
                    }
                }
        ));
    }

}
