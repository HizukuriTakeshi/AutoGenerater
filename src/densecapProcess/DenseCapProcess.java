package densecapProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fileUtils.FileUtils;

public class DenseCapProcess {

	public static void DenseCapScript1() throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder("sh","scripts/densecap1.sh");

		pb.redirectErrorStream(true); //デフォルトはfalse：マージしない（標準出力と標準エラーは別々）

		Process process = null;
		process = pb.start();
		//Processの使い方は後述
		int ret = 0;
		ret = process.waitFor();
		System.out.println("戻り値：" + ret);
		
		InputStream is = process.getInputStream();	//標準出力
		printInputStream(is);
		process.waitFor();
		
		
		File in = new File("/home/hizukuri/densecap/vis/data/results.json");
		File out = new File("./imgs/results1.json");
		try {
			FileUtils.copyFile(in, out);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
	}
	
	public static void DenseCapScript2() throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder("sh","scripts/densecap2.sh");

		pb.redirectErrorStream(true); //デフォルトはfalse：マージしない（標準出力と標準エラーは別々）

		Process process = null;
		process = pb.start();
		//Processの使い方は後述
		int ret = 0;
		ret = process.waitFor();
		System.out.println("戻り値：" + ret);
		
		InputStream is = process.getInputStream();	//標準出力
		printInputStream(is);
		process.waitFor();
		

		File in = new File("/home/hizukuri/densecap/vis/data/results.json");
		File out = new File("./imgs/results.json");
		try {
			FileUtils.copyFile(in, out);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
	}
	
	public static void DenseCapScript3() throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder("sh","scripts/densecap3.sh");

		pb.redirectErrorStream(true); //デフォルトはfalse：マージしない（標準出力と標準エラーは別々）

		Process process = null;
		process = pb.start();
		//Processの使い方は後述
		int ret = 0;
		ret = process.waitFor();
		System.out.println("戻り値：" + ret);
		
		InputStream is = process.getInputStream();	//標準出力
		printInputStream(is);
		process.waitFor();
		
		
		File in = new File("/home/hizukuri/densecap/vis/data/results.json");
		File out = new File("./imgs/processed/results3.json");
		try {
			FileUtils.copyFile(in, out);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
	}
	
	public static void printInputStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			for (;;) {
				String line = br.readLine();
				if (line == null) break;
				System.out.println(line);
			}
		} finally {
			br.close();
		}
	}
}
