package org.overmind.resourcer.reader.extension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.ParameterResolutionException;

public final class ResourceReader {

  private ResourceReader() {}

  public static String asString(String path, Charset charset) {
    InputStream inputStream = asInputStream(path);

    try {
      return IOUtils.toString(inputStream, charset);
    } catch (IOException e) {
      throw new ParameterResolutionException("Exception during reading resource file", e);
    }
  }

  public static InputStream asInputStream(String path) {
    URL url = getResourceURL(path);

    try {
      return url.openStream();
    } catch (IOException e) {
      throw new ParameterResolutionException("Exception during resolve resource file", e);
    }
  }

  public static Path asPath(String path) {
    URL url = getResourceURL(path);

    try {
      return Paths.get(url.toURI());
    } catch (URISyntaxException e) {
      throw new ParameterResolutionException("Exception during resolve resource file", e);
    }
  }

  private static URL getResourceURL(String path) {
    ClassLoader classLoader = ResourceReader.class.getClassLoader();

    URL resource = classLoader.getResource(path);

    if (resource == null) {
      throw new ParameterResolutionException("Resource with given path has not found");
    }

    return resource;
  }
}
