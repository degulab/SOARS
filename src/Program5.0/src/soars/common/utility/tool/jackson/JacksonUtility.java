/**
 * 
 */
package soars.common.utility.tool.jackson;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kurata
 * JSONデータ文字列<-->任意のクラス変換ユーティリティークラス
 */
public class JacksonUtility {

	/**
	 * 任意のクラスからJSON文字列データを生成して返す
	 * @param object任意のクラス
	 * @return
	 */
	public static String get_json(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString( object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * JSONデータ文字列から指定したクラスのインスタンスを生成して返す
	 * @param json JSONデータ文字列
	 * @param cls 指定クラス
	 * @return
	 */
	public static Object get_object(String json, Class cls) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue( json, cls);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 任意のクラスからJSON文字列データを生成してファイルへ書き込む
	 * @param object任意のクラス
	 * @return
	 */
	public static boolean write_json(File file, Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue( file, object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * JSONデータ文字列をファイルから読み込んで指定したクラスのインスタンスを生成して返す
	 * @param file ファイル
	 * @param cls 指定クラス
	 * @return
	 */
	public static Object get_object(File file, Class cls) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue( file, cls);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * JSONデータ文字列をファイルから読み込んで指定した型のインスタンスを生成して返す
	 * @param file ファイル
	 * @param typeReference 型
	 * @return
	 */
	public static Object get_object(File file, TypeReference typeReference) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue( file, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
