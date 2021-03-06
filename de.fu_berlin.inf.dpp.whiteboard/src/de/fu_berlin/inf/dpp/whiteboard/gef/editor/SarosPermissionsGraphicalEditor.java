package de.fu_berlin.inf.dpp.whiteboard.gef.editor;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.project.AbstractSarosSessionListener;
import de.fu_berlin.inf.dpp.project.ISarosSessionListener;
import de.fu_berlin.inf.dpp.project.SarosSessionManager;
import de.fu_berlin.inf.dpp.session.AbstractSharedProjectListener;
import de.fu_berlin.inf.dpp.session.ISarosSession;
import de.fu_berlin.inf.dpp.session.ISharedProjectListener;
import de.fu_berlin.inf.dpp.session.User;
import de.fu_berlin.inf.dpp.ui.util.SWTUtils;

/**
 * The editor is enabled/disabled respective the Saros user permissions,
 * read-only or write-access.
 * 
 * @author jurke
 * 
 */
public abstract class SarosPermissionsGraphicalEditor extends
		BlockableGraphicalEditor {

	protected ISarosSessionListener sessionListener = new AbstractSarosSessionListener() {
		@Override
		public void sessionStarted(ISarosSession session) {
			session.addListener(sharedProjectListener);
			setEnabledInSWTThread(session.getLocalUser().hasWriteAccess());
		}

		@Override
		public void sessionEnded(ISarosSession session) {
			session.removeListener(sharedProjectListener);
			setEnabledInSWTThread(true);
		}
	};

	protected ISharedProjectListener sharedProjectListener = new AbstractSharedProjectListener() {
		@Override
		public void permissionChanged(User user) {
			if (user.isLocal()) {
				setEnabledInSWTThread(user.hasWriteAccess());
			}
		}
	};

	private void setEnabledInSWTThread(final boolean enable) {
		SWTUtils.runSafeSWTAsync(null, new Runnable() {

			@Override
			public void run() {
				SarosPermissionsGraphicalEditor.this.setEnabled(enable);
			}
		});
	}

	@Inject
	private SarosSessionManager sessionManager;

	public SarosPermissionsGraphicalEditor() {
		SarosPluginContext.reinject(this);
		sessionManager.addSarosSessionListener(sessionListener);
	}

	/*
	 * If the view is opened after session start, we have to check the
	 * permissions.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		ISarosSession session = sessionManager.getSarosSession();
		if (session != null) {
			setEnabled(session.getLocalUser().hasWriteAccess());
		}
	}

}
