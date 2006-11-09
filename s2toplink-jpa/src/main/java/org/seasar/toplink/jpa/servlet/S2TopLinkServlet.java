package org.seasar.toplink.jpa.servlet;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.servlet.S2ContainerServlet;
import org.seasar.framework.jpa.impl.PersistenceUnitManagerImpl;

public class S2TopLinkServlet extends S2ContainerServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2482862697371051842L;
    
    public static final String PREDEPLOY_CONFIG = "predeployConfig";
    
    public static final String USE_STATIC_CONTEXT = "useStaticContext";
    
    public static final String DEFAULT_PREDEPLOY_CONFIG_PATH = "entityManager.dicon";

    @Override
    public void init() {
        String useStaticContext = getServletConfig().getInitParameter(USE_STATIC_CONTEXT);
        if (useStaticContext != null && Boolean.parseBoolean(useStaticContext)) {
            String originalConfigPath = SingletonS2ContainerFactory.getConfigPath();
            try {
                String predeployConfig = DEFAULT_PREDEPLOY_CONFIG_PATH;
                String initParamConfig = getServletConfig().getInitParameter(PREDEPLOY_CONFIG);
                if (initParamConfig != null) {
                    predeployConfig = initParamConfig;
                }
                SingletonS2ContainerFactory.setConfigPath(predeployConfig);
                SingletonS2ContainerFactory.init();
                S2Container container = SingletonS2ContainerFactory.getContainer();
                container.findAllComponents(EntityManagerFactory.class);
            } finally {
                SingletonS2ContainerFactory.destroy();
                SingletonS2ContainerFactory.setConfigPath(originalConfigPath);
            }
        }
        super.init();
    }

    @Override
    public void destroy() {
        String useStaticContext = getServletConfig().getInitParameter(USE_STATIC_CONTEXT);
        if (useStaticContext != null && Boolean.parseBoolean(useStaticContext)) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            PersistenceUnitManagerImpl pumi =
                PersistenceUnitManagerImpl.class.cast(container.getComponent(PersistenceUnitManagerImpl.class));
            pumi.setUseStaticContext(false);
        }
        super.destroy();
    }

}
