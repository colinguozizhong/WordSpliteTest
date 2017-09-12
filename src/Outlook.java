
/**
 * JACOB Outlook sample contributed by
 * Christopher Brind <christopher.brind@morse.com> 
 */

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Outlook {

	public static void main(String asArgs[]) throws Exception {
		File folder = new File("C:\\TEST");
		//File folder = new File("C:\\Users\\Administrator\\Desktop\\Desktop\\ZCWJ_DOC");
		for (File file : folder.listFiles()) {
			System.out.println(file.getAbsolutePath());
			String filePath = file.getAbsolutePath();
			office2Pdf(filePath, filePath.substring(0, filePath.indexOf(".")) + ".pdf");
		}
	}

	public static void office2Pdf(String sourceDir, String destDir) {
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			ComThread.InitSTA();
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			doc = Dispatch.invoke(docs, "Open", Dispatch.Method,
					new Object[] { sourceDir, new Variant(false), new Variant(true), // 是否只读
							new Variant(false), new Variant("pwd") },
					new int[1]).toDispatch();
			// Dispatch.put(doc, "Compatibility", false); //兼容性检查,为特定值false不正确
			Dispatch.put(doc, "RemovePersonalInformation", false);
			Dispatch.call(doc, "ExportAsFixedFormat", destDir, 17); // word保存为pdf格式宏，值为17
			System.out.println("success!!!"); // set flag true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc != null) {
				Dispatch.call(doc, "Close", false);
			}
			if (app != null) {
				app.invoke("Quit", new Variant[] {});
			}
			ComThread.Release();
		}
	}

}
