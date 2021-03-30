package soars2.model.COVID-19.scenario13.utils.analysis2019.logger2013;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * エントリが１行のみのログを作成するためのロガー． シミュレーションの最後に何らかの結果のデータを記録するのに利用すると便利である．
 * 
 * @author isao
 *
 */
public class TCSingleEntryLogger {

	/** ログを出力するかどうか？ */
	private boolean fActive;

	/** ファイル名 */
	private String fFilename;

	/** キー集合 */
	private ArrayList<String> fKeys;

	/** データ */
	private HashMap<String, Object> fData;

	/**
	 * コンストラクタ
	 * 
	 * @param activeFlag ログを出力するかどうか？
	 * @param filename   ファイル名．実際のログファイル名は，このファイル名に".csv"を付加したものになる．
	 */
	public TCSingleEntryLogger(boolean activeFlag, String filename) {
		fActive = activeFlag;
		fFilename = filename;
		fKeys = new ArrayList<String>();
		fData = new HashMap<String, Object>();
	}

	/**
	 * データをログに登録する．すでにキーが存在すれば，そのキーに対応するデータが上書きされる． データがプリミティブ型の場合は，そのまま表に書き込まれる．
	 * データがテキスト型（StringWriter型）の場合は，プロパティファイルで指定したファイル名に"_Key.txt"ファイルが作成されてテキストが書き込まれる．表には"ファイル名_Key.txt"が書き込まれる．
	 * データ型がオブジェクト型の場合は，プロパティファイルで指定したファイル名に"_Key.obj"ファイルが作成されてバイナリデータが書き込まれる．表には"ファイル名_Key.obj"が書き込まれる．
	 * 
	 * @param key  キー
	 * @param data データ
	 * @throws IOException
	 */
	public void putData(String key, Object data) throws IOException {
		if (!fActive) {
			return;
		}
		if (!fData.containsKey(key)) {
			fKeys.add(key);
		}
		if (data instanceof Number || data instanceof Boolean || data instanceof String) {
			fData.put(key, data.toString());
		} else if (data instanceof StringWriter) {
			String filename = fFilename + "_" + key + ".txt";
			fData.put(key, filename);
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			StringWriter sw = (StringWriter) data;
			pw.print(sw.toString());
			pw.close();
		} else {
			String filename = fFilename + "_" + key + ".obj";
			fData.put(key, filename);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(data);
			oos.close();
		}
	}

	/**
	 * 表をファイルに出力する．
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		if (!fActive) {
			return;
		}
		PrintWriter pw = new PrintWriter(fFilename + ".csv");
		for (int i = 0; i < fKeys.size(); ++i) {
			pw.print(fKeys.get(i));
			if (i < fKeys.size() - 1) {
				pw.print(",");
			} else {
				pw.println();
			}
		}
		for (int i = 0; i < fKeys.size(); ++i) {
			pw.print(fData.get(fKeys.get(i)));
			if (i < fKeys.size() - 1) {
				pw.print(",");
			} else {
				pw.println();
			}
		}
		pw.close();
	}

}
