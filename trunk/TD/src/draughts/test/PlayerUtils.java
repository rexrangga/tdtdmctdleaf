package draughts.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import draughts.ITD;
import draughts.TDLambda;
import draughts.TDLeaf;
import draughts.TDMC;

public class PlayerUtils {

	private static final double a = 99;
	private static final double b = 0.027;

	private static final double alpha = 0.0001;
	private static final double gamma = 1;
	private static final double lambda = 0.9;

	enum PlayerKind {
		TD, TD_LEAF, TDMC;
	}
	
	public static PlayerKind getKind(int which) {
		switch (which) {
		case (0):
			return PlayerKind.TD;
		case (1):
			return PlayerKind.TD_LEAF;
		}
		return PlayerKind.TDMC;
	}

	public static ITD loadPlayer(String filePath, PlayerKind kind) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String line = in.readLine();
			String[] tokens = line.split(" ");
			if (tokens.length != 12) {
				throw new IOException("wrong file format: expected 12 weights but got " + tokens.length);
			}
			double[] weights = new double[12];
			for (int i = 0; i < 12; i++) {
				try {
					weights[i] = Double.parseDouble(tokens[i]);
				} catch (NumberFormatException e) {
					throw new IOException("wrong file format: " + tokens[i] + " is not a valid double value");
				}
			}
			switch (kind) {
			case TD:
				return new TDLambda(weights, a, b, alpha, gamma, lambda);
			case TD_LEAF:
				return new TDLeaf(weights, a, b, alpha, gamma, lambda);
			case TDMC:
				return new TDMC(weights, gamma, lambda, alpha, a, b);
			default:
				throw new IllegalArgumentException(kind + " is not a valid learining algorithm");
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static ITD createRandomPlayer(PlayerKind kind) {
		Random rand = new Random();
		double[] weights = new double[12];
		for (int i = 0; i < 12; i++) {
			weights[i] = rand.nextDouble() * 10 - 5;
		}
		switch (kind) {
		case TD:
			return new TDLambda(weights, a, b, alpha, gamma, lambda);
		case TD_LEAF:
			return new TDLeaf(weights, a, b, alpha, gamma, lambda);
		case TDMC:
			return new TDMC(weights, gamma, lambda, alpha, a, b);
		default:
			throw new IllegalArgumentException(kind + " is not a valid learining algorithm");
		}
	}

	public static void savePlayer(ITD player, String filePath) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filePath));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < player.getWeigths().length; i++) {
				sb.append(player.getWeigths()[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			out.write(sb.toString());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
