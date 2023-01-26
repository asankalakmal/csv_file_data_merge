import java.util.Arrays;
import exception.MergerException;
import services.MergerService;

public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		// your code starts here.
		System.out.print("Starting file merge process, file Names :");
		Arrays.stream(args).forEach(x->{
			System.out.print(" "+x);
		});
		System.out.println("");

		try {
			MergerService mergeService = new MergerService(args, FILENAME_COMBINED);
			mergeService.mergeFiles();
			System.out.println("File merge process successfully completed !");
		} catch(MergerException ex) {
			System.err.println("Unexpected Error happen while procesing the files:: "+ex);
			System.out.println("File merge process unccessfull!");
		}
		
	}

}
