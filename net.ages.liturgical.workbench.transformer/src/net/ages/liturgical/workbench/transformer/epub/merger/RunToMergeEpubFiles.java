package net.ages.liturgical.workbench.transformer.epub.merger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ages.liturgical.workbench.transformer.epub.Attributes;
import net.ages.liturgical.workbench.transformer.utils.AlwbFileUtils;
import net.ages.liturgical.workbench.transformer.utils.GeneralUtils;
import net.ages.liturgical.workbench.transformer.utils.PropertyUtils;

/**
 * Run this to build combine (merge) ePub files
 * with special conditions.  If you just want ePubs
 * by service month, use RunToMergeEpubFilesGroupedByMonth.
 * 
 * @author mac002
 *
 */
public class RunToMergeEpubFiles {

	public static void main(String[] args) {
		try {
			
			// read the properties
			String ePubConfig = "/Transformer.config";
			PropertyUtils props = new PropertyUtils(ePubConfig);
			String pathToServicesIndexHtml = props.getPropString("pathToServicesIndexHtml");
			String source = GeneralUtils.getParentPath(pathToServicesIndexHtml);
			source = source + "/e/"; 
			String title = props.getPropString("merge.title");
			String author = props.getPropString("merge.author");
			String mergeFilename = props.getPropString("merge.filename");
			List<String> patterns = props.getPropArray("merge.regular.expression");
			List<String> exclusions = props.getListFromDelimitedString("merge.exclusions");
			List<String> fileSortOrder = props.getPropArray("epub.merge.file");
				
			// get the files that match the patterns
			List<File> files = AlwbFileUtils.getMatchingFilesInDirectory(source, patterns,"epub");
			
			Map<String,File> fileMap = new TreeMap<String,File> ();
			for (File f : files) {
				fileMap.put(f.getName(), f);
			}

			files.clear();
			for (String epub : fileSortOrder) {
				try {
					files.add(fileMap.get(epub));
				} catch (Exception e) {
					System.out.println("epub.merge.file = " + epub + " did not find a matching file.");
				}
			}
			// Create the EpubMerger.  Instantiating it will invoke the merge process.
			EpubMerger merger = new EpubMerger(
					files
					, title
					, author
					, source  + "m/"
					, mergeFilename
					, exclusions
					, Attributes.VALUE_TYPE_AD_HOC
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
