package soars2.model.covid19.scenario13.utils.analysis2019.logger2013;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * 複数行のエントリからなるログを作成するためのロガー． 時系列ログを作成するの便利なロガーである。
 * ログデータをCSV形式で出力する．列をキーで，行をエントリーIDで管理する． beginLogメソッドでロギングを開始する．
 * beginEntryメソッドで表のエントリー（行）を作成を開始し，必要なだけputDataメソッドでデータを追加して，endEntryメソッドでエントリーの作成を終了する．
 * beginEntryメソッドで，自動的にエントリーID（キーはID）がエントリーの先頭に書き込まれる．
 * endEntryメソッドで，エントリーに登録されたデータがファイルに出力され，エントリーIDが＋１される．
 * putDataメソッドに渡したデータがプリミティブ型（文字列型も含む）の場合は，表のセルに直接書き込まれる．
 * putDataメソッドに渡したデータがオブジェクト型の場合は，ファイル名を「ログファイル名_Key_EntryID.obj」としたオブジェクトファイルを別途生成し出力する．表のセルにはファイル名が書き込まれる．
 * putDataメソッドに渡したデータがテキスト型（StringWriter型）の場合は，ファイル名を「ログファイル名_Key_EntryID.txt」としたテキストファイルを別途生成し出力する．表のセルにはファイル名が書き込まれる．
 * ここで，Keyはユーザが指定したキー，EntryIDはエントリーIDである． engLogメソッドでロギングを終了する．
 * 実際にログデータが出力される条件は， 1)
 * ロガーがアクティブであること（システムによりsetActiveメソッドで設定されるので，ユーザが設定する必要はない），かつ， 2)
 * エントリがアクティブであること(beginEntryメソッドで引数にtrueを指定した場合に相当），かつ， 3)
 * キーがアクティブであること（コンストラクタの引数activeKeysで設定）， である．
 * ループのネストに対応するため，プロパティファイルで子ロガーを設定している場合は，createChildメソッドにより子ロガーを生成して，サブループに渡すことができる．
 * createChildメソッドが呼ばれると，createChildメソッドの引数のキーで子ロガーのファイル名が表のセルに書き込まれる．
 * 子ロガーのファイル名は，親ロガーのファイル名に"_Key_EntryID.csv"が付加されたものとなる．
 * createChildメドッドの引数のキーがアクティブキーでない場合，子ロガーは非アクティブになり，子ロガーのログファイルは生成されない．
 * プロパティファイル内で複数の子ロガーを指定している場合は，createChildメソッドの引数のloggerNameで生成する子ロガーの名前を与える．
 * もし複数の子ロガーを指定しているにもかかわらず，loggerNameを与えなかった場合は，プロパティ内で指定した一番先頭の子ロガーが生成される．
 *
 * @author isao, uemura
 *
 */
public class TCMultiEntryLogger {

	/** ロガーの識別名．子ロガーを複数種類用いる際に，子ロガー生成時に指定する必要がある． */
	private String fLoggerName;

	/** このロガーでログを出力するかどうかのフラグ */
	private boolean fActiveFlag;

	/** ログファイル名．実際には，このファイル名に".csv"をつけたものとなる． */
	private String fFilename;

	/** ログ出力対象のキーの集合 */
	private String[] fActiveKeys;

	/** 子ロガーのテンプレート */
	private TCMultiEntryLogger[] fChildLoggers;

	/** ログデータ．１エントリー（行）ごとにファイルに出力されて，クリアされる． */
	private HashMap<String, String> fData;

	/** 現在のタイミングでログを出力するかどうかのフラグ */
	private boolean fDataOutputFlag;

	/** 出力ストリーム */
	private PrintWriter fOutputStream;

	/** エントリーID．endEntryメソッドが呼ばれるたびに＋１される． */
	private long fEntryID;

	/**
	 * コンストラクタ．
	 * 
	 * @param filename   ログファイル名．実際には拡張子".csv"が付加される．オブジェクトファイルは"_[Key]_[EntryID].csv"，テキストファイルは"_[Key]_[EntryID].csv"が付加される．テキストファイルは，putDataメソッドにStringWriterオブジェクトを渡したときに生成される．
	 * @param activeKeys ログ出力対象のデータのキーの集合．
	 */
	public TCMultiEntryLogger(String filename, String[] activeKeys) {
		this(null, filename, activeKeys, (TCMultiEntryLogger[]) null);
	}

	/**
	 * コンストラクタ．
	 * 
	 * @param filename     ログファイル名．実際には拡張子".csv"が付加される．オブジェクトファイルは"_[Key]_[EntryID].obj"，テキストファイルは"_[Key]_[EntryID].txt"が付加される．テキストファイルは，putDataメソッドにStringWriterオブジェクトを渡したときに生成される．
	 * @param activeKeys   ログ出力対象のデータのキーの集合．
	 * @param childLoggers 子ロガー
	 */
	public TCMultiEntryLogger(String filename, String[] activeKeys, TCMultiEntryLogger... childLoggers) {
		this(null, filename, activeKeys, childLoggers);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param loggerName   ロガーの識別名．上位ロガーが複数の子ロガーを指定している場合，それぞれの識別のために用いられる．
	 * @param filename     ログファイル名．実際には拡張子".csv"が付加される．オブジェクトファイルは"_[Key]_[EntryID].obj"，テキストファイルは"_[Key]_[EntryID].txt"が付加される．テキストファイルは，putDataメソッドにStringWriterオブジェクトを渡したときに生成される．
	 * @param activeKeys   ログ出力対象のデータのキーの集合．
	 * @param childLoggers 子ロガー
	 */
	public TCMultiEntryLogger(String loggerName, String filename, String[] activeKeys,
			TCMultiEntryLogger... childLoggers) {
		fLoggerName = loggerName;
		fFilename = filename;
		fActiveKeys = activeKeys;
		fChildLoggers = childLoggers;
		fDataOutputFlag = false;
		fData = new HashMap<String, String>();
		fActiveFlag = true;
		fEntryID = -1;
	}

	/**
	 * クローンを生成する．
	 * 
	 * @return クローン
	 */
	private TCMultiEntryLogger createClone() {
		return new TCMultiEntryLogger(fFilename, fActiveKeys, fChildLoggers);
	}

	/**
	 * ログの出力/非出力を設定する．
	 * 
	 * @param activeFlag true:出力する, false:出力しない
	 */
	public void setActive(boolean activeFlag) {
		fActiveFlag = activeFlag;
	}

	/**
	 * ログを出力するか？
	 * 
	 * @return true:出力する, false:出力しない
	 */
	public boolean isActive() {
		return fActiveFlag;
	}

	/**
	 * 子ロガーを生成する． プロパティファイルで子ロガーを複数生成している場合，引数loggerNameで生成する子ロガーの名前を指定する．
	 * 
	 * @param key        子ロガーのキー
	 * @param loggerName 子ロガーの識別名
	 * @return 子ロガー
	 * @throws IOException
	 */
	public TCMultiEntryLogger createChild(String key, String loggerName) throws IOException {
		TCMultiEntryLogger child = null;
		for (TCMultiEntryLogger childLogger : fChildLoggers) {
			if (childLogger.getLoggerName().equals(loggerName)) {
				child = childLogger.createClone();
				break;
			}
		}
		if (child == null) {
			throw new IOException("ChildLogger not found.");
		}
		String childFilename = fFilename + "_" + key + "_" + fEntryID;
		child.setFilename(childFilename);
		if (isActiveKey(key) && fDataOutputFlag) {
			putData(key, childFilename + ".csv");
			child.setActive(true);
		} else {
			child.setActive(false);
		}
		return child;
	}

	/**
	 * 子ロガーを生成する． プロパティファイルで子ロガーを複数指定している場合， 一番先頭で指定している子ロガーが生成される．
	 * 明示的に生成する子ロガーを指定する場合は {@link #createChild(String key, String loggerName)}
	 * を利用すること．
	 * 
	 * @param key 子ロガーのキー
	 * @return
	 * @throws IOException
	 */
	public TCMultiEntryLogger createChild(String key) throws IOException {
		TCMultiEntryLogger child = fChildLoggers[0].createClone();
		String childFilename = fFilename + "_" + key + "_" + fEntryID;
		child.setFilename(childFilename);
		if (isActiveKey(key) && fDataOutputFlag) {
			putData(key, childFilename + ".csv");
			child.setActive(true);
		} else {
			child.setActive(false);
		}
		return child;
	}

	/**
	 * ファイル名を設定する．
	 * 
	 * @param filename ファイル名
	 */
	public void setFilename(String filename) {
		fFilename = filename;
	}

	/**
	 * ログ出力を開始する．
	 * 
	 * @throws IOException
	 */
	public void beginLog() throws IOException {
		if (!isActive()) {
			return;
		}
		fOutputStream = new PrintWriter(fFilename + ".csv");
		writeHeader();
		fEntryID = 0;
	}

	/**
	 * ログ出力を終了する．
	 */
	public void endLog() {
		if (!isActive()) {
			return;
		}
		fOutputStream.close();
		fOutputStream = null;
		fEntryID = -1;
	}

	/**
	 * ログのエントリ（表の行）の出力を開始する．
	 * 
	 * @param activeFlag true:実際にエントリをログに出力する、false:実際にはエントリをログに出力しない。
	 */
	public void beginEntry(boolean activeFlag) {
		fData.clear();
		fDataOutputFlag = false;
		if (isActive() && activeFlag) {
			fDataOutputFlag = true;
			fData.put("id", fEntryID + "");
		}
	}

	/**
	 * ログのエントリを終了する．
	 */
	public void endEntry() {
		if (!isActive() || !fDataOutputFlag) {
			return;
		}
		fOutputStream.print(fData.get("id"));
		for (String activeKey : fActiveKeys) {
			String data = fData.get(activeKey);
			if (data == null) {
				fOutputStream.print(",");
			} else {
				fOutputStream.print("," + data);
			}
		}
		fOutputStream.println();
		fOutputStream.flush();
		++fEntryID;
	}

	/**
	 * データをログに登録する．
	 * 
	 * @param key  キー
	 * @param data データ
	 * @throws IOException
	 */
	public void putData(String key, Object data) throws IOException {
		if (!isActiveKey(key)) {
			return;
		}
		if (!isActive() || !fDataOutputFlag) {
			return;
		}
		if (data instanceof Number || data instanceof Boolean || data instanceof String) {
			fData.put(key, data.toString());
		} else if (data instanceof StringWriter) {
			String filename = fFilename + "_" + key + "_" + fEntryID + ".txt";
			fData.put(key, filename);
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			StringWriter sw = (StringWriter) data;
			pw.print(sw.toString());
			pw.close();
		} else {
			String filename = fFilename + "_" + key + "_" + fEntryID + ".obj";
			fData.put(key, filename);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(data);
			oos.close();
		}
	}

	/**
	 * ロガー名を返す．
	 * 
	 * @return
	 */
	public String getLoggerName() {
		return fLoggerName;
	}

	/**
	 * キーがアクティブかどうか？
	 * 
	 * @param key キー
	 * @return true:アクティブである，false:アクティブでない
	 */
	private boolean isActiveKey(String key) {
		for (String activeKey : fActiveKeys) {
			if (activeKey.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ヘッダーをログファイルに書き込む．
	 */
	private void writeHeader() {
		fOutputStream.print("id");
		for (String activeKey : fActiveKeys) {
			fOutputStream.print("," + activeKey);
		}
		fOutputStream.println();
	}

}
