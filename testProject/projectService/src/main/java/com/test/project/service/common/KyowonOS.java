package com.test.project.service.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public final class KyowonOS {
    public static void rmdir(File dir) throws KyowonException {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                rmdir(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    public static void rmfile(String file) throws KyowonException {
        File del = new File(file);
        del.delete();
    }

    public static void rmdir(String dir) throws KyowonException {
        rmdir(new File(dir));
    }

    public static void cpdir(File src, File dest) throws KyowonException {
        dest.mkdirs();
        File[] files = src.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            File destFile = new File(dest.getPath() + File.separatorChar + file.getName());
            if (file.isDirectory()) {
                cpdir(file, destFile);
                continue;
            }
            try {
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(destFile);
                IOUtils.copy(in, out);
                in.close();
                out.close();
            } catch (IOException ex) {
                throw new KyowonException("Could not copy file: " + file, ex);
            }
        }
    }

    public static void cpdir(String src, String dest) throws KyowonException {
        cpdir(new File(src), new File(dest));
    }

    public static void exec(String[] cmd) throws KyowonException {
        Process ps = null;
        try {
            ps = Runtime.getRuntime().exec(cmd);

            new StreamForwarder(ps.getInputStream(), System.err).start();
            new StreamForwarder(ps.getErrorStream(), System.err).start();
            if (ps.waitFor() != 0) {
                throw new KyowonException("could not exec command: " + Arrays.toString(cmd));
            }
        } catch (IOException ex) {
            throw new KyowonException("could not exec command: " + Arrays.toString(cmd), ex);
        } catch (InterruptedException ex) {
            throw new KyowonException("could not exec command: " + Arrays.toString(cmd), ex);
        }
    }

    public static File createTempDirectory() throws KyowonException {
        try {
            File tmp = File.createTempFile("BRUT", null);
            if (!tmp.delete()) {
                throw new KyowonException("Could not delete tmp file: " + tmp.getAbsolutePath());
            }
            if (!tmp.mkdir()) {
                throw new KyowonException("Could not create tmp dir: " + tmp.getAbsolutePath());
            }
            return tmp;
        } catch (IOException ex) {
            throw new KyowonException("Could not create tmp dir", ex);
        }
    }

    static class StreamForwarder extends Thread {

        public StreamForwarder(InputStream in, OutputStream out) {
            mIn = in;
            mOut = out;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(mIn));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(mOut));
                String line;
                while ((line = in.readLine()) != null) {
                    out.write(line);
                    out.newLine();
                }
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private final InputStream  mIn;
        private final OutputStream mOut;
    }
}
