package nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Vector {
	int n;
	double v[];
	/******************/
	/* コンストラクタ */
	/******************/
	Vector (double[] v, int n1)
	{
		this.v = v;
		this.n = n1;
	}
	/**********/
	/* 絶対値 */
	/**********/
	double zettai()
	{
		double x = 0.0;
		int i1;
		for (i1 = 0; i1 < n; i1++)
			x += v[i1] * v[i1];
		return Math.sqrt(x);
	}
	/*********************/
	/* 内積              */
	/*      b : ベクトル */
	/*********************/
	double naiseki(Vector b)
	{
		double x = 0.0;
		int i1;
		for (i1 = 0; i1 < n; i1++)
			x += v[i1] * b.v[i1];
		return x;
	}
	/********/
	/* 入力 */
	/********/
	void input() throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int i1;
		for (i1 = 0; i1 < n; i1++) {
			System.out.print((i1+1) + "番目の要素は？ ");
			v[i1] = Double.parseDouble(in.readLine());
		}
	}
}
