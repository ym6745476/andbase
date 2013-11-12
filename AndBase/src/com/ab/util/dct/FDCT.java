package com.ab.util.dct;

public class FDCT implements DCT {

	public static double[][] fDctTransform(double[][] ablk) {
		double[][] blk = new double[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				blk[i][j] = ablk[i][j];
			}
		}
		// 对行变换
		for (int i = 0; i <= 7; i++) {
			double S07, S16, S25, S34, S0734, S1625;
			double D07, D16, D25, D34, D0734, D1625;
			S07 = blk[i][0] + blk[i][7];
			S16 = blk[i][1] + blk[i][6];
			S25 = blk[i][2] + blk[i][5];
			S34 = blk[i][3] + blk[i][4];
			S0734 = S07 + S34;
			S1625 = S16 + S25;
			D07 = blk[i][0] - blk[i][7];
			D16 = blk[i][1] - blk[i][6];
			D25 = blk[i][2] - blk[i][5];
			D34 = blk[i][3] - blk[i][4];
			D0734 = S07 - S34;
			D1625 = S16 - S25;
			blk[i][0] = 0.5 * (C4 * (S0734 + S1625));
			blk[i][1] = 0.5 * (C1 * D07 + C3 * D16 + C5 * D25 + C7 * D34);
			blk[i][2] = 0.5 * (C2 * D0734 + C6 * D1625);
			blk[i][3] = 0.5 * (C3 * D07 - C7 * D16 - C1 * D25 - C5 * D34);
			blk[i][4] = 0.5 * (C4 * (S0734 - S1625));
			blk[i][5] = 0.5 * (C5 * D07 - C1 * D16 + C7 * D25 + C3 * D34);
			blk[i][6] = 0.5 * (C6 * D0734 - C2 * D1625);
			blk[i][7] = 0.5 * (C7 * D07 - C5 * D16 + C3 * D25 - C1 * D34);
		}
		// 对列变换
		for (int j = 0; j <= 7; j++) {
			double S07, S16, S25, S34, S0734, S1625;
			double D07, D16, D25, D34, D0734, D1625;
			S07 = blk[0][j] + blk[7][j];
			S16 = blk[1][j] + blk[6][j];
			S25 = blk[2][j] + blk[5][j];
			S34 = blk[3][j] + blk[4][j];
			S0734 = S07 + S34;
			S1625 = S16 + S25;
			D07 = blk[0][j] - blk[7][j];
			D16 = blk[1][j] - blk[6][j];
			D25 = blk[2][j] - blk[5][j];
			D34 = blk[3][j] - blk[4][j];
			D0734 = S07 - S34;
			D1625 = S16 - S25;
			blk[0][j] = 0.5 * (C4 * (S0734 + S1625));
			blk[1][j] = 0.5 * (C1 * D07 + C3 * D16 + C5 * D25 + C7 * D34);
			blk[2][j] = 0.5 * (C2 * D0734 + C6 * D1625);
			blk[3][j] = 0.5 * (C3 * D07 - C7 * D16 - C1 * D25 - C5 * D34);
			blk[4][j] = 0.5 * (C4 * (S0734 - S1625));
			blk[5][j] = 0.5 * (C5 * D07 - C1 * D16 + C7 * D25 + C3 * D34);
			blk[6][j] = 0.5 * (C6 * D0734 - C2 * D1625);
			blk[7][j] = 0.5 * (C7 * D07 - C5 * D16 + C3 * D25 - C1 * D34);
		}
		return blk;
	}
}
