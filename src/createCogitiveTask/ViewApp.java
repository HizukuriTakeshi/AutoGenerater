package createCogitiveTask;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.opencv.core.Core;

import densecapProcess.DenseCapProcess;
import fileUtils.FileUtils;

public class ViewApp {

	private JFrame frame;


	private DataListList datalistList;
	private DataListList onlycaption;



	private File in1;
	private File in2;

	private String resultdirPath;
	private String resultappeardirPath;
	private String resultdisappeardirPath;
	
	
	private final Action action = new SwingAction();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewApp window = new ViewApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//
	/**
	 * Create the application.
	 */
	public ViewApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 213, 144);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(action);
		btnNewButton.setBounds(12, 12, 189, 92);
		frame.getContentPane().add(btnNewButton);

		datalistList = new DataListList();

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}
	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "自動生成開始");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser(); // ファイル選択用クラス

			int selected = filechooser.showOpenDialog(frame); //「開く」ダイアログ表示

			File inputfile = filechooser.getSelectedFile();			


			ArrayList<String[]> array = new ArrayList<String[]>();

			//テキストファイルから複数の入力画像のファイルを読み込む
			try {
				//ファイルを読み込む
				FileReader fr = new FileReader(inputfile.getPath());
				BufferedReader br = new BufferedReader(fr);

				//読み込んだファイルを１行ずつ処理する
				String line;


				int i = 0;
				while ((line = br.readLine()) != null) {
					//区切り文字","で分割する
					array.add(line.split(","));

					//分割した文字を画面出力する
					System.out.println(array.get(i)[0]+":"+array.get(i)[1]);
					//System.out.println("**********");
					i++;
				}

				//終了処理
				br.close();

			} catch (IOException ex) {
				//例外発生時処理
				ex.printStackTrace();
			}


			if (selected == JFileChooser.APPROVE_OPTION){ //ファイルが選択されたら
				
				System.out.println("生成開始");
				for(String[] s: array){
				Generate(s[0],s[1]);
				}
				System.out.println("生成終了");
			}
		}
	}

	public void Generate(String path1, String path2){

		//入力画像の読み込み		
		in1 = new File(path1);
		File out1 = new File("./imgs/"+in1.getName());
		try {
			FileUtils.copyFile(in1, out1);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		in2 = new File(path2);
		File out2 = new File("./imgs/"+in2.getName());
		try {
			FileUtils.copyFile(in2, out2);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}				

		//結果を入れるためのディレクトリ作成
		File processeddir = new File("./imgs/result/"+in1.getName()+"_"+in2.getName());
		processeddir.mkdir();
		resultdirPath=processeddir.getPath();
		
		String filePath1 = "./imgs/tmp1/";
		String filePath2 = "./imgs/tmp2/";

		//densecap処理用のディレクトリ
		File tmpdir1 = new File(filePath1);
		tmpdir1.mkdir();

		File out3 = new File(filePath1+in1.getName());
		try {
			FileUtils.copyFile(in1, out3);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		File tmpdir2 = new File(filePath2);
		tmpdir2.mkdir();
		File out4 = new File(filePath2+in2.getName());
		try {
			FileUtils.copyFile(in2, out4);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		//densecap処理
		try {
			DenseCapProcess.DenseCapScript1();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		try {
			DenseCapProcess.DenseCapScript2();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		FileUtils.delete(tmpdir1);
		FileUtils.delete(tmpdir2);

		String j1= "./imgs/results1.json";
		String j2= "./imgs/results.json";
		
		File appear = new File(resultdirPath+"/appearance");
		appear.mkdir();
		resultappeardirPath=appear.getPath();
		
		File disappear = new File(resultdirPath+"/disappearance");
		disappear.mkdir();
		resultdisappeardirPath=disappear.getPath();
		
		File file1 = new File(j1);
		File file2 = new File(j2);

		File out5 = new File(resultappeardirPath+"/results.json");
		try {
			FileUtils.copyFile(file1, out5);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		File out6 = new File(resultdisappeardirPath+"/results.json");
		try {
			FileUtils.copyFile(file2, out6);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}


		if (file1.exists() && file2.exists()){ //ファイルが選択されたら


			JsonDataReader jsonReader = new JsonDataReader();//JSON形式ファイル読み込みクラス


			DenseCapList densecapList1 = new DenseCapList();
			densecapList1 = jsonReader.jsonDataRead(j1);//ファイルから読み込む

			//二つ目のjsonファイルを読み込み、datalistlist1に datalistlist2のdatalistを追加することで一つのデータリストリストに過去現在の説明文を入れる。
			DenseCapList densecapList2 = new DenseCapList();
			densecapList2 = jsonReader.jsonDataRead(j2);//ファイルから読み込む

			DataListList tmp = new DataListList();

			tmp = densecapList2.toDataListList();

			datalistList = densecapList1.toDataListList();
			datalistList.addDataList(tmp.getDatalistList().get(0));

			//説明文飲みの出現文推定
			onlycaption = new DataListList();

			DenseCapList densecapList3 = new DenseCapList();
			densecapList3 = jsonReader.jsonDataRead(j1);//ファイルから読み込む

			//二つ目のjsonファイルを読み込み、datalistlist1に datalistlist2のdatalistを追加することで一つのデータリストリストに過去現在の説明文を入れる。
			DenseCapList densecapList4 = new DenseCapList();
			densecapList4 = jsonReader.jsonDataRead(j2);//ファイルから読み込む
			DataListList tmp2 = new DataListList();
			tmp2 = densecapList4.toDataListList();

			//文字だけの比較
			onlycaption = densecapList3.toDataListList();
			onlycaption.addDataList(tmp2.getDatalistList().get(0));
			CompareCap cconlycap = new CompareCap();
			cconlycap.compareAllCaption(onlycaption);

			try {
				//出力先を作成する
				FileWriter fw = new FileWriter(resultappeardirPath+"/mojionly_appearance.txt", false);  //※１
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				pw.println("apearance");
				//内容を指定する
				for(Data d: cconlycap.getDatalist0().getDatas()){
					pw.println(d.getCaption());
				}
				//ファイルに書き出す
				pw.close();
			} catch (IOException ex) {
				//例外時処理
				ex.printStackTrace();
			}
			
			try {
				//出力先を作成する
				FileWriter fw = new FileWriter(resultdisappeardirPath+"/mojionly_disappearance.txt", false);  //※１
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				pw.println("disapearance");
				for(Data d: cconlycap.getDatalist1().getDatas()){
					pw.println(d.getCaption());
				}
				//ファイルに書き出す
				pw.close();
			} catch (IOException ex) {
				//例外時処理
				ex.printStackTrace();
			}


			//差分と説明文BB比較
			CompareBoundingBox cbb = new CompareBoundingBox();
			cbb.compareBoundingBox(datalistList);

			//候補とキャプション群比較
			CompareCap cc = new CompareCap();
			cc.checkCaption0(datalistList);

			try {
				//出力先を作成する
				FileWriter fw2 = new FileWriter(resultappeardirPath+"/sabun_appearance.txt", false);  //※１
				PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));

				//内容を指定する
				for(Data d:datalistList.getDatalistList().get(1).getDatas()){
					if(d.getType() == QuestionType.APPEARANCE && d.getLink() == -1){
						pw2.println(d.getCaption());
					}
				}
				//ファイルに書き出す
				pw2.close();
				//終了メッセージを画面に出力する
				System.out.println("出現物体の候補出力が完了しました。");
			} catch (IOException ex) {
				//例外時処理
				ex.printStackTrace();
			}
			
			try {
				//出力先を作成する
				FileWriter fw2 = new FileWriter(resultdisappeardirPath+"/sabun_disappearance.txt", false);  //※１
				PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));

				//内容を指定する
				for(Data d:datalistList.getDatalistList().get(0).getDatas()){
					if(d.getType() == QuestionType.DISAPPEARANCE && d.getLink() == -1){
						pw2.println(d.getCaption());
					}
				}
				//ファイルに書き出す
				pw2.close();
				//終了メッセージを画面に出力する
				System.out.println("消失物体の候補出力出力が完了しました。");
			} catch (IOException ex) {
				//例外時処理
				ex.printStackTrace();
			}


		}

	}

}

