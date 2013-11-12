package com.ab.util.dct;

public class IFDCT  implements DCT {

		public static double[][] iFDctTransform(double[][] ablk) {
			double[][] blk = new double[8][8];
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					blk[i][j] = ablk[i][j];
				}
			}
			// 对列做IDCT
			for (int j = 0; j <= 7; j++) {
				double[] tmp = new double[16];
				// first step
				tmp[0] = blk[0][j] * C4 + blk[2][j] * C2;
				tmp[1] = blk[4][j] * C4 + blk[6][j] * C6;
				tmp[2] = blk[0][j] * C4 + blk[2][j] * C6;
				tmp[3] = -blk[4][j] * C4 - blk[6][j] * C2;
				tmp[4] = blk[0][j] * C4 - blk[2][j] * C6;
				tmp[5] = -blk[4][j] * C4 + blk[6][j] * C2;
				tmp[6] = blk[0][j] * C4 - blk[2][j] * C2;
				tmp[7] = blk[4][j] * C4 - blk[6][j] * C6;
				tmp[8] = blk[1][j] * C7 - blk[3][j] * C5;
				tmp[9] = blk[5][j] * C3 - blk[7][j] * C1;
				tmp[10] = blk[1][j] * C5 - blk[3][j] * C1;
				tmp[11] = blk[5][j] * C7 + blk[7][j] * C3;
				tmp[12] = blk[1][j] * C3 - blk[3][j] * C7;
				tmp[13] = -blk[5][j] * C1 - blk[7][j] * C5;
				tmp[14] = blk[1][j] * C1 + blk[3][j] * C3;
				tmp[15] = blk[5][j] * C5 + blk[7][j] * C7;
				// second step
				tmp[0] = 0.5 * (tmp[0] + tmp[1]);
				tmp[1] = 0.5 * (tmp[2] + tmp[3]);
				tmp[2] = 0.5 * (tmp[4] + tmp[5]);
				tmp[3] = 0.5 * (tmp[6] + tmp[7]);
				tmp[4] = 0.5 * (tmp[8] + tmp[9]);
				tmp[5] = 0.5 * (tmp[10] + tmp[11]);
				tmp[6] = 0.5 * (tmp[12] + tmp[13]);
				tmp[7] = 0.5 * (tmp[14] + tmp[15]);
				// third step
				blk[0][j] = tmp[0] + tmp[7];
				blk[1][j] = tmp[1] + tmp[6];
				blk[2][j] = tmp[2] + tmp[5];
				blk[3][j] = tmp[3] + tmp[4];
				blk[4][j] = tmp[3] - tmp[4];
				blk[5][j] = tmp[2] - tmp[5];
				blk[6][j] = tmp[1] - tmp[6];
				blk[7][j] = tmp[0] - tmp[7];
			}
			// 对行做IDCT
			for (int i = 0; i <= 7; i++) {
				double[] tmp = new double[16];
				// first step
				tmp[0] = blk[i][0] * C4 + blk[i][2] * C2;
				tmp[1] = blk[i][4] * C4 + blk[i][6] * C6;
				tmp[2] = blk[i][0] * C4 + blk[i][2] * C6;
				tmp[3] = -blk[i][4] * C4 - blk[i][6] * C2;
				tmp[4] = blk[i][0] * C4 - blk[i][2] * C6;
				tmp[5] = -blk[i][4] * C4 + blk[i][6] * C2;
				tmp[6] = blk[i][0] * C4 - blk[i][2] * C2;
				tmp[7] = blk[i][4] * C4 - blk[i][6] * C6;
				tmp[8] = blk[i][1] * C7 - blk[i][3] * C5;
				tmp[9] = blk[i][5] * C3 - blk[i][7] * C1;
				tmp[10] = blk[i][1] * C5 - blk[i][3] * C1;
				tmp[11] = blk[i][5] * C7 + blk[i][7] * C3;
				tmp[12] = blk[i][1] * C3 - blk[i][3] * C7;
				tmp[13] = -blk[i][5] * C1 - blk[i][7] * C5;
				tmp[14] = blk[i][1] * C1 + blk[i][3] * C3;
				tmp[15] = blk[i][5] * C5 + blk[i][7] * C7;
				// second step
				tmp[0] = 0.5 * (tmp[0] + tmp[1]);
				tmp[1] = 0.5 * (tmp[2] + tmp[3]);
				tmp[2] = 0.5 * (tmp[4] + tmp[5]);
				tmp[3] = 0.5 * (tmp[6] + tmp[7]);
				tmp[4] = 0.5 * (tmp[8] + tmp[9]);
				tmp[5] = 0.5 * (tmp[10] + tmp[11]);
				tmp[6] = 0.5 * (tmp[12] + tmp[13]);
				tmp[7] = 0.5 * (tmp[14] + tmp[15]);
				// third step
				blk[i][0] = tmp[0] + tmp[7];
				blk[i][1] = tmp[1] + tmp[6];
				blk[i][2] = tmp[2] + tmp[5];
				blk[i][3] = tmp[3] + tmp[4];
				blk[i][4] = tmp[3] - tmp[4];
				blk[i][5] = tmp[2] - tmp[5];
				blk[i][6] = tmp[1] - tmp[6];
				blk[i][7] = tmp[0] - tmp[7];
			}
			return blk;
		}

}
