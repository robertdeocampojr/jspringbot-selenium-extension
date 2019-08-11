package com.jspringbot.selenium.extension;


import org.jspringbot.syntax.HighlightRobotLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by robertdeocampo on 04/01/2019.
 */
public class UtilityHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(UtilityHelper.class);




    public UtilityHelper() {
    }



    public void deleteFile(String filepath){
        File file = new File(filepath);
        if(file.delete()){
            LOG.keywordAppender().appendArgument("Deleted: ", filepath);
        }
        else {
            LOG.keywordAppender().appendArgument("File not exist: ", filepath);
        }
    }

    public String getMatchString(String strValue, String regex){
        String str = strValue;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        String matched = null;
        while (matcher.find()) {
            matched = matcher.group(1);
        }
        System.out.println(matched);
        return matched;
    }


}
