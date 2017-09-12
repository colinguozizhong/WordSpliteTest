import java.io.File;


public class test2 {
	public static void main(String [] args){
		File folder = new File("E:\\SZFXM\\M_ZCWJ");
		for (File file : folder.listFiles()) {
			System.out.println(file.getAbsolutePath());
			String filePath = file.getAbsolutePath();
			file.renameTo(new File(file.getAbsolutePath().replaceAll(".doc", ".pdf")));
		}
	}
}
