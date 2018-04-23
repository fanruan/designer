package com.fr.start.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * p: 这是为了将一个OutputStream输出多个OutputStream
 */
public class MultiOutputStream extends OutputStream {
  	private List outList = new ArrayList();
	
	public MultiOutputStream() {
	}
	
	public void addOutputStream(OutputStream output) {
		this.outList.add(output);
	}
	
	public void removeOutputStream(OutputStream output) {
		this.outList.remove(output);
	}
	
	public int getOutputStreamCount() {
		return this.outList.size();
	}
	
	public OutputStream getOutputStream(int index) {
		return (OutputStream) this.outList.get(index);
	}

	public void write(int b) throws IOException {
		for(int i = 0; i < outList.size(); i++) {
			((OutputStream)outList.get(i)).write(b);
		}
	}

	public void write(byte buff[]) throws IOException {
		for(int i = 0; i < outList.size(); i++) {
			((OutputStream)outList.get(i)).write(buff);
		}
	}
	
	public void write(byte buff[], int off, int len) throws IOException {
		for(int i = 0; i < outList.size(); i++) {
			((OutputStream)outList.get(i)).write(buff, off, len);
		}
	}
	
	public void flush() throws IOException {
		for(int i = 0; i < outList.size(); i++) {
			((OutputStream)outList.get(i)).flush();
		}
	}

	public void close() throws IOException {
		for(int i = 0; i < outList.size(); i++) {
			((OutputStream)outList.get(i)).close();
		}		
	}
}