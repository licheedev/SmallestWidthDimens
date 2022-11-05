package com.licheedev.commonsize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static void main(String[] args) throws Exception {
        Config config = new Config(new File("generator_config.properties"));
        Generator generator = new Generator(config);

        // 先打扫
        File root = new File(".");
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(String.valueOf(file));
                if (file.isDirectory() && file.getName().startsWith(Config.MODULE_PREFIX)) {
                    FileUtil.removeFile(file);
                }
            }
        }

        if (config.justDelete) {
            return;
        }

        File templet = new File(Config.TEMPLET);

        List<String> modules = new ArrayList<>();
        modules.add("app");
        modules.add("generator");

        for (Integer designWidth : config.designWidthList) {
            String moduleName = Config.MODULE_PREFIX + designWidth;

            File destModule = new File(moduleName);
            FileUtil.copyDir(templet, destModule);

            File resDir = new File(destModule, Config.RES_ROOT);
            // 先生成个默认的
            generator.generateFile(resDir, designWidth, 0);
            // 再生成其他
            for (Integer integer : config.swList) {
                generator.generateFile(resDir, designWidth, integer);
            }

            System.out.println(designWidth);

            modules.add(moduleName);
        }

        File settingsGradle = new File("settings.gradle");
        FileWriter settingsGradleWriter = new FileWriter(settingsGradle, false);

        for (String module : modules) {
            settingsGradleWriter.write(String.format("include ':%s'\n", module));
        }
        settingsGradleWriter.flush();
    }

    private final Config mConfig;
    private final String mDpLine;
    private final String mNegDpLine;
    private final String mSpLine;

    public Generator(Config config) {
        mConfig = config;
        mDpLine = Config.DIMEN_DP_LINE.replace(Config.REPLACE_REG, mConfig.dpResReg);
        mNegDpLine = Config.DIMEN_DP_LINE.replace(Config.REPLACE_REG, "_" + mConfig.dpResReg);
        mSpLine = Config.DIMEN_SP_LINE.replace(Config.REPLACE_REG, mConfig.spResReg);
    }

    private void generateFile(File resRoot, int designWidth, int smallestWidth) {
        File dir;
        if (smallestWidth > 0) {
            dir = new File(resRoot, "values-sw" + smallestWidth + "dp");
        } else {
            dir = new File(resRoot, "values");
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, mConfig.fileName);

        StringBuilder sb = new StringBuilder();
        sb.append(Config.XML_HEAD).append("\n").append(Config.RESOURCES_START).append("\n");

        // dp
        for (Integer i : mConfig.dpRange) {
            float value;
            if (smallestWidth > 0) {
                value = i / (float) designWidth * smallestWidth;
            } else {
                value = i / (float) designWidth * 360;
            }

            if (i < 0) {
                sb.append(mConfig.intent).append(replaceLine(mNegDpLine, i, value)).append("\n");
            } else {
                sb.append(mConfig.intent).append(replaceLine(mDpLine, i, value)).append("\n");
            }
        }

        // sp
        for (Integer i : mConfig.spRange) {
            float value;
            if (smallestWidth > 0) {
                value = i / (float) designWidth * smallestWidth;
            } else {
                value = i / (float) designWidth * 360;
            }

            sb.append(mConfig.intent).append(replaceLine(mSpLine, i, value)).append("\n");
        }

        sb.append(Config.RESOURCES_END).append("\n");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceLine(String lineReg, int x, float value) {
        if (x == 0) {
            return lineReg.replace("{x}", "" + Math.abs(x)).replace("{value}", "0");
        } else {
            return lineReg.replace("{x}", "" + Math.abs(x)).replace("{value}", "" + value);
        }
    }
}
