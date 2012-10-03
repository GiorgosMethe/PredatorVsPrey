package matPack;

import java.io.IOException;
import java.util.ArrayList;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;

public class MatFileGenerator {
	
	public static void write(double[] src,String outputFileName) throws IOException{
		
	 MLDouble mlDouble = new MLDouble( "Steps", src, 1 );
	 
	 //2. write arrays to file
	 ArrayList list = new ArrayList();
	 list.add( mlDouble );
	 
	 new MatFileWriter( outputFileName+".mat", list );
	 
	}

}
