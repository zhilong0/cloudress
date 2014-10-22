package com.tt.blobstore.bundle.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.tt.blobstore.bundle.Blob;
import com.tt.blobstore.bundle.BlobStoreException;
import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;
import com.tt.blobstore.bundle.BundleValue;

public class FileSystemBundleService implements BundleService {

	private File directory;

	public FileSystemBundleService(File directory) {
		if (directory == null) {
			throw new BlobStoreException("Directory must not be null");
		}
		if (!directory.exists()) {
			directory.mkdirs();
		} else {
			if (!directory.isDirectory()) {
				throw new BlobStoreException("File %s is not a directory", directory.getAbsolutePath());
			}
		}

		this.directory = directory;
	}

	@Override
	public void addBlob(Blob blob, BundleKey bundleKey) {
		if (bundleKey == null) {
			throw new IllegalArgumentException("Bundle Key must not be null");
		}
		if (!(bundleKey instanceof FileSystemBundleKey)) {
			throw new IllegalArgumentException("Bundle Key must be type of " + FileSystemBundleKey.class.getName());
		}
		String relativePath = bundleKey.getKeyInBundle().toString();
		File file = new File(directory, relativePath);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(file, true));
			BundleValue bundleValue = blob.getBundleValue();
			InputStream input = null;
			if (bundleValue != null) {
				input = blob.getBundleValue().getDataInBundle();
			} else {
				input = new ByteArrayInputStream(new byte[0]);
			}
			try {
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = input.read(buffer)) > 0) {
					out.write(buffer, 0, size);
				}
			} finally {
				out.flush();
				out.close();
				input.close();
			}
		} catch (IOException ex) {
			String msg = "Add blob %s error";
			throw new BlobStoreException(ex, msg, relativePath);
		}
	}

	@Override
	public boolean fetchBlob(Blob blob, BundleKey key) {
		String fileName = key.getKeyInBundle().toString();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(directory, fileName)));
			blob.readBundleValue(in);
			return true;
		} catch (FileNotFoundException ex) {
			return false;
		} catch (IOException ex) {
			String msg = "get blob %s error";
			throw new BlobStoreException(ex, msg, fileName);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	@Override
	public void deleteBlob(BundleKey key) {
		String fileName = key.getKeyInBundle().toString();
		File file = new File(directory, fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public boolean hasBlob(BundleKey key) {
		String fileName = key.getKeyInBundle().toString();
		File file = new File(directory, fileName);
		return file.exists();
	}

	public File getDirectory() {
		return directory;
	}

	@Override
	public void updateBlob(Blob blob, BundleKey key) {
		this.addBlob(blob, key);
	}

}
