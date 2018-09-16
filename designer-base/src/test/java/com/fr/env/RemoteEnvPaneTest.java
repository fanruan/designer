//package com.fr.env;
//
//import org.fest.swing.annotation.RunsInEDT;
//import org.fest.swing.core.ComponentLookupScope;
//import org.fest.swing.core.Robot;
//import org.fest.swing.edt.GuiQuery;
//import org.fest.swing.fixture.JPanelFixture;
//import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
//import org.junit.Test;
//
//import javax.swing.JFrame;
//import javax.swing.WindowConstants;
//
//import static org.fest.swing.edt.GuiActionRunner.execute;
//
//public class RemoteEnvPaneTest extends FestSwingJUnitTestCase {
//
//    @Override
//    protected void onSetUp() {
//
//    }
//
//    @Test
//    public void test() {
//
//        Robot robot = robot();
//
//        robot.settings().componentLookupScope(ComponentLookupScope.ALL);
//
//        JPanelFixture rootFixture = new JPanelFixture(robot, createNewEditor());
//        rootFixture.checkBox("httpsCheckbox").check();
//
//        rootFixture.checkBox("httpsCheckbox").requireSelected();
//    }
//
//    @RunsInEDT
//    private static RemoteEnvPane createNewEditor() {
//
//        return execute(new GuiQuery<RemoteEnvPane>() {
//            @Override
//            protected RemoteEnvPane executeInEDT() {
//                RemoteEnvPane envPane = new RemoteEnvPane();
//                JFrame frame = new JFrame();
//                frame.getContentPane().add(envPane);
//                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//                frame.setResizable(false);
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//                return envPane;
//            }
//        });
//    }
//}
