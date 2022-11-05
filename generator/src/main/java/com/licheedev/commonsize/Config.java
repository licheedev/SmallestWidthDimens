package com.licheedev.commonsize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by John on 2018/3/5.
 */

public class Config {

    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    public static final String REPLACE_REG = "{reg}";
    public static final String REPLACE_VALUE = "{value}";
    public static final String DIMEN_DP_LINE =
        "<dimen name=\"" + REPLACE_REG + "\">" + REPLACE_VALUE + "dp</dimen>";
    public static final String DIMEN_SP_LINE =
        "<dimen name=\"" + REPLACE_REG + "\">" + REPLACE_VALUE + "sp</dimen>";

    public static final String RESOURCES_START = "<resources>";
    public static final String RESOURCES_END = "</resources>";

    public static final String TEMPLET = "templet";
    public static final String RES_ROOT = "src/main/res";
    public static final String MODULE_PREFIX = "sw_";

    public String fileName = "sw_dimens.xml";
    public String intent = getBlank(4);
    public String dpResReg = "normal_{x}dp";
    public String spResReg = "normal_{x}sp";
    public Range dpRange = new Range(-1080, 1080);
    public Range spRange = new Range(1, 100);

    public List<Integer> designWidthList;
    public List<Integer> swList;
    public boolean justDelete;

    public Config(File configPropertiesFile) {

        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(configPropertiesFile);
            properties.load(input);
            input.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        String designWidthList = properties.getProperty("design_width_list", "");
        this.designWidthList = parseDesignWidths(designWidthList);

        fileName = properties.getProperty("file_name");
        int intentLength = Integer.parseInt(properties.getProperty("intent_length", "4"));
        intent = getBlank(intentLength);
        dpResReg = properties.getProperty("dp_res_reg");
        spResReg = properties.getProperty("sp_res_reg");

        try {
            dpRange = Range.parse(properties.getProperty("dp_range"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            spRange = Range.parse(properties.getProperty("sp_range"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        String swListFile = properties.getProperty("smallest_width_list");
        swList = parseSwList(new File(swListFile));

        justDelete = Boolean.parseBoolean(properties.getProperty("just_delete", "false"));
    }

    private static String getBlank(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static List<Integer> parseDesignWidths(String designWidthList) {
        ArrayList<Integer> designWidths = new ArrayList<>();

        try {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(designWidthList);
            while (matcher.find()) {
                String sizeStr = matcher.group();
                int designWidth = Integer.parseInt(sizeStr);
                designWidths.add(designWidth);
            }
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }

        return designWidths;
    }

    private static ArrayList<Integer> parseSwList(File file) {
        ArrayList<Integer> sizes = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                if (tmp.startsWith("//")) {
                    continue;
                }

                Integer size = -1;
                try {
                    int _pos = tmp.indexOf("//");
                    if (_pos > 0) {
                        size = Integer.parseInt(tmp.substring(0, _pos).trim());
                    } else {
                        size = Integer.parseInt(tmp.trim());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (size > 0) {
                    sizes.add(size);
                }
            }
        } catch (IOException e) {
            //            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sizes;
    }
}
