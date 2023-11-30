package com.front;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CommandLineArgument {

    public void processCommandLine(String args[]) throws InterruptedException {
        String USAGE = "scurl [option] url";
        String DEFAULT_PATH = "src/main/resources/index.json";

        Options cliOptions = new Options();
        cliOptions.addOption(new Option("applicationName", "an", true,
                "프로그램 옵션으로 Application Name을 줄 수 있으며, application name이 주어질 경우 해당 메시지만 수신하도록 한다."));
        cliOptions.addOption(new Option("s", true, "허용 가능한 센서 종류를 지정할 수 있다."));
        cliOptions.addOption(
                new Option("c", false, "설정 파일과 command line argument라 함께 주어질 경우 command line argument가 우선된다."));
        cliOptions.addOption(new Option("h", "help", false, "사용법, 옵션을 보여줍니다."));

        HelpFormatter helpFormatter = new HelpFormatter();
        JSONObject object = new JSONObject();

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine c = parser.parse(cliOptions, args);

            if (c.hasOption("h"))
                helpFormatter.printHelp(USAGE, cliOptions);

            if (c.hasOption("c")) {
                JSONParser jsonParser = new JSONParser();
                Reader reader;

                if (c.getOptionValue("c") == null) {
                    reader = new FileReader(DEFAULT_PATH);
                } else {
                    reader = new FileReader(c.getOptionValue("c"));
                }

                try {
                    object = (JSONObject) jsonParser.parse(reader);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }
            }

            if (c.hasOption("s")) {
                if (c.getOptionValue("s") != null) {
                    String[] arr = c.getOptionValue("s").split(",");
                    object.put("sensor", Arrays.toString(arr));
                }
            }

            if (c.hasOption("applicationName")) {
                if (c.getOptionValue("applicationName") != null) {
                    object.put("applicationName", c.getOptionValue("applicationName"));
                }

            }

        } catch (ParseException e) {
            helpFormatter.printHelp(USAGE, cliOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}