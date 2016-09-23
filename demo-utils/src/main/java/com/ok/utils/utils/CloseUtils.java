package com.ok.utils.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 * @description 关闭stream、reader、writer等的工具类
 * @author li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class CloseUtils {
	//private static final String TAG = "CloseUtils";
	
	public static void close(InputStream is) {
		if(is == null)
			return;
		
		try {
			is.close();
		} catch (Exception e) {

		}
	}

	public static void close(OutputStream os) {
		if(os == null)
			return;
		
		try {
			os.flush();
			os.close();
		} catch (Exception e) {

		}
	}

	public static void close(RandomAccessFile randomAccessFile) {
		if (randomAccessFile == null)
			return;
		
		try {
			randomAccessFile.close();
		} catch (Exception e) {

		}
	}
	
	public static void close(Writer writer) {
		if (writer == null)
			return;
		
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {

		}
	}

	public static void close(Reader reader) {
		if(reader == null)
			return;
		
		try {
			reader.close();
		} catch (Exception e) {

		}
	}

	public static void close(Cursor cursor) {
		if(cursor == null)
			return;

		try {
			cursor.close();
		} catch (Exception e) {

		}
	}

	public static void close(SQLiteDatabase db) {
		if(db == null)
			return;

		try {
			db.close();
		} catch (Exception e) {

		}
	}
}
