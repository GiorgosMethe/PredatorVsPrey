package ioPack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class ValueLoader {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double[][] readValues = ReadValues();
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {

				readValues[i][j] = i * j * 0.7;

			}

		}

		WriteValues(readValues);

	}

	public static double[][] ReadValues() throws FileNotFoundException {

		double[][] Values = new double[11][11];

		try {

			FileInputStream fstream = new FileInputStream("Values.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i = 0;
			while ((strLine = br.readLine()) != null) {

				String[] row = strLine.split(" ");
				for (int j = 0; j < row.length; j++) {
					Values[i][j] = Double.parseDouble(row[j]);
				}
				i++;
			}

			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		return Values;

	}

	public static void WriteValues(double[][] Values) throws IOException {

		Writer output = null;
		String OutputValues = "";
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {

				OutputValues += String.valueOf(Values[i][j]) + " ";

			}
			OutputValues += "\r\n";
		}

		File file = new File("Values.txt");
		output = new BufferedWriter(new FileWriter(file));
		output.write(OutputValues);
		output.close();

	}

}
