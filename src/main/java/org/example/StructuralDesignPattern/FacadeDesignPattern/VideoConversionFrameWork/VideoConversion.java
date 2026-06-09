package org.example.StructuralDesignPattern.FacadeDesignPattern.VideoConversionFrameWork;

import java.io.File;

// Subsystem classes (complex framework)
class VideoFile {
    private final String filename;

    public VideoFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}

interface Codec {}

class OggCompressionCodec implements Codec {
    public String getType() {
        return "ogg";
    }
}

class MPEG4CompressionCodec implements Codec {
    public String getType() {
        return "mp4";
    }
}

class CodecFactory {
    public static Codec extract(VideoFile file) {
        if (file.getFilename().endsWith(".mp4")) {
            return new MPEG4CompressionCodec();
        } else {
            return new OggCompressionCodec();
        }
    }
}

class BitrateReader {
    public static String read(VideoFile file, Codec codec) {
        System.out.println("Reading file: " + file.getFilename());
        return "buffer";
    }

    public static String convert(String buffer, Codec codec) {
        System.out.println("Converting buffer using codec");
        return "converted-buffer";
    }
}

class AudioMixer {
    public String fix(String result) {
        System.out.println("Fixing audio");
        return result + "-with-audio";
    }
}

// Facade class
class VideoConverter {

    public File convert(String filename, String format) {
        System.out.println("VideoConverter: conversion started.");

        VideoFile file = new VideoFile(filename);
        Codec sourceCodec = CodecFactory.extract(file);

        Codec destinationCodec;
        if ("mp4".equalsIgnoreCase(format)) {
            destinationCodec = new MPEG4CompressionCodec();
        } else {
            destinationCodec = new OggCompressionCodec();
        }

        String buffer = BitrateReader.read(file, sourceCodec);
        String result = BitrateReader.convert(buffer, destinationCodec);
        result = new AudioMixer().fix(result);

        System.out.println("VideoConverter: conversion completed.");
        return new File("output." + format);
    }
}

class Client {
    public Client(){
        VideoConverter converter = new VideoConverter();
        File mp4 = converter.convert("./funny-cat-video.ogg", "mp4");

        System.out.println("Saved file: " + mp4.getName());
    }
}

// Client code
public class VideoConversion {
    public static void main(String[] args) {
        Client c = new Client();
    }
}
