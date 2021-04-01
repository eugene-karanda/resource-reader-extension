package org.overmind.resourcer.reader.extension;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ResourceReaderExtension implements ParameterResolver {

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.isAnnotated(Resource.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {

    Resource annotation =
        parameterContext
            .findAnnotation(Resource.class)
            .orElseThrow(
                () -> new ParameterResolutionException("@Resource required but not found"));

    Class<?> type = parameterContext.getParameter().getType();
    String path = annotation.value();
    Charset charset = Charset.forName(annotation.charset());

    if (type == String.class) {
      return ResourceReader.asString(path, charset);
    } else if (type == InputStream.class) {
      return ResourceReader.asInputStream(path);
    } else if (type == Path.class) {
      return ResourceReader.asPath(path);
    }

    throw new ParameterResolutionException("Unsupported type: " + type);
  }
}
