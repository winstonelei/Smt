package com.github.ftpDownloader;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * 命令行参数解析工具类
 */
public class CommandParamParser {
    @Option(name = "-start", usage = "指定下载数据的开始日期！")
    private String startDay = "";
    @Option(name = "-end", usage = "指定下载数据的结束日期！")
    private String endDay = "";
    @Option(name = "-localPath", required = true, usage = "指定下载数据本地存放路径！")
    private String localPath;


    public static CommandParamParser commandLineParamAnalysis(String[] args) {
        return CommandLineAnalysis.commandLineParam(args);
    }

    private static class CommandLineAnalysis {
        private static CommandParamParser commandLineParam(String[] args) {
            CommandParamParser param = new CommandParamParser();
            CmdLineParser parser = new CmdLineParser(param);

            try {
                parser.parseArgument(args);
            } catch (CmdLineException e) {
                showHelp(parser);
                System.exit(1);
            }
            return param;
        }

        private static void showHelp(CmdLineParser parser) {
            System.out.println("请按要求指定参数列表：");
            parser.printUsage(System.out);
        }
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public String getLocalPath() {
        return localPath;
    }


    @Override
    public String toString() {
        return "CommandParamParser{" +
                "startDay='" + startDay + '\'' +
                ", endDay='" + endDay + '\'' +
                ", localPath='" + localPath + '\'' +
                '}';
    }
}
