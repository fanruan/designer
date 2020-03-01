package com.fr.design.mainframe;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class JFormTest {
    
    @Test
    public void testRun() {
    
        JForm mockForm = EasyMock.partialMockBuilder(JForm.class).addMockedMethod("getUndoManager").createMock();
        try {
            mockForm.canUndo();
        } catch (Exception e) {
            Assert.fail();
        }
    }
    
}