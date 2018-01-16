package com.newexcavator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.attribute.DocAttributeSet;

public class InputStreamDoc implements Doc {
	private String filename;
	private DocFlavor docFlavor;
	private InputStream stream;

	public InputStreamDoc(String name, DocFlavor flavor) {
		filename = name;
		docFlavor = flavor;
	}

	public DocFlavor getDocFlavor() {
		return docFlavor;
	}

	/* No attributes attached to this Doc - mainly useful for MultiDoc */
	public DocAttributeSet getAttributes() {
		return null;
	}

	/*
	 * Since the data is to be supplied as an InputStream delegate to getStreamForBytes().
	 */
	public Object getPrintData() throws IOException {
		return getStreamForBytes();
	}

	/* Not possible to return a GIF as text */
	public Reader getReaderForText() throws UnsupportedEncodingException, IOException {
		return null;
	}

	/*
	 * Return the print data as an InputStream. Always return the same instance.
	 */
	public InputStream getStreamForBytes() throws IOException {
		synchronized (this) {
			if (stream == null) {
				stream = new FileInputStream(filename);
			}
			return stream;
		}
	}
}