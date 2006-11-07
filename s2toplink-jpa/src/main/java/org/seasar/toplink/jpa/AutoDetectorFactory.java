package org.seasar.toplink.jpa;

import java.util.List;

import org.seasar.framework.autodetector.ResourceAutoDetector;

public interface AutoDetectorFactory {
    
    List<ResourceAutoDetector> getResourceAutoDetectorList(String unitName);
}
