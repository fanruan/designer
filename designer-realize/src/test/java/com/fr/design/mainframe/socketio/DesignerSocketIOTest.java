package com.fr.design.mainframe.socketio;

import com.fr.invoke.Reflect;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author: Maksim
 * @Date: Created in 2019/12/9
 * @Description:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkContext.class,DesignerSocketIO.class, IO.class})
public class DesignerSocketIOTest {

    @Test
    public void close() throws Exception {
        this.update();
        DesignerSocketIO.close();
        DesignerSocketIO.Status status = Reflect.on(DesignerSocketIO.class).field("status").get();
        Socket socket = Reflect.on(DesignerSocketIO.class).field("socket").get();

        Assert.assertEquals(DesignerSocketIO.Status.Disconnecting,status);
        Assert.assertNull(socket);
    }

    @Test
    public void update() throws Exception {
        Workspace current = EasyMock.mock(Workspace.class);
        EasyMock.expect(current.isLocal()).andReturn(false);

        PowerMock.mockStatic(WorkContext.class);
        EasyMock.expect(WorkContext.getCurrent()).andReturn(current);

        String[] uri = {"http://127.0.0.1:8888/workspace","http://127.0.0.1:9999/workspace"};
        PowerMock.mockStaticPartial(DesignerSocketIO.class,"getSocketUri");
        PowerMock.expectPrivate(DesignerSocketIO.class,"getSocketUri").andReturn(uri);

        EasyMock.replay(current);
        PowerMock.replayAll();

        DesignerSocketIO.update();
        DesignerSocketIO.Status status = Reflect.on(DesignerSocketIO.class).field("status").get();
        Socket socket = Reflect.on(DesignerSocketIO.class).field("socket").get();

        Assert.assertEquals(DesignerSocketIO.Status.Connected,status);
        Assert.assertNotNull(socket);
    }
}