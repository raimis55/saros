package de.fu_berlin.inf.dpp.ui.expressions;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.filesystem.ResourceAdapterFactory;
import de.fu_berlin.inf.dpp.project.ISarosSessionManager;
import de.fu_berlin.inf.dpp.session.ISarosSession;

/**
 * Adds tests to the {@link IResource}. <br/>
 * Currently tests whether given {@link IResource} is part of the
 * {@link ISarosSession}.
 */
public class ProjectPropertyTester extends PropertyTester {

    @Inject
    ISarosSessionManager sarosSessionManager;

    public ProjectPropertyTester() {
        SarosPluginContext.initComponent(this);
    }

    @Override
    public boolean test(Object receiver, String property, Object[] args,
        Object expectedValue) {
        if (receiver instanceof IResource) {
            IResource resource = (IResource) receiver;
            if ("isInSarosSession".equals(property)) {
                ISarosSession sarosSession = sarosSessionManager
                    .getSarosSession();
                if (sarosSession != null) {
                    if (resource instanceof IProject)
                        return sarosSession
                            .isCompletelyShared(ResourceAdapterFactory
                                .create((IProject) resource));
                    return sarosSession.isShared(ResourceAdapterFactory
                        .create(resource));
                }
            }
        }
        return false;
    }

}
