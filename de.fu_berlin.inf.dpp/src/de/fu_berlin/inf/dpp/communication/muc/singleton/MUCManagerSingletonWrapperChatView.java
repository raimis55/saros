package de.fu_berlin.inf.dpp.communication.muc.singleton;

import org.apache.log4j.Logger;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.communication.muc.MUCManager;
import de.fu_berlin.inf.dpp.communication.muc.negotiation.MUCSessionPreferences;
import de.fu_berlin.inf.dpp.communication.muc.negotiation.MUCSessionPreferencesNegotiatingManager;
import de.fu_berlin.inf.dpp.communication.muc.session.MUCSession;
import de.fu_berlin.inf.dpp.project.AbstractSarosSessionListener;
import de.fu_berlin.inf.dpp.project.ISarosSession;
import de.fu_berlin.inf.dpp.project.ISarosSessionListener;
import de.fu_berlin.inf.dpp.project.ISarosSessionManager;
import de.fu_berlin.inf.dpp.ui.views.SarosView;
import de.fu_berlin.inf.dpp.util.Utils;

/**
 * This class handles the creation and destruction of the {@link MUCSession}
 * used by the {@link SarosView}.
 * 
 * @author bkahlert
 */
@Component(module = "communication")
public class MUCManagerSingletonWrapperChatView extends
    MUCManagerSingletonWrapper {
    private static final Logger log = Logger
        .getLogger(MUCManagerSingletonWrapperChatView.class);

    protected ISarosSessionManager sarosSessionManager;

    @Inject
    protected MUCSessionPreferencesNegotiatingManager mucSessionPreferencesNegotiationManager;

    protected MUCSessionPreferences preferences;

    protected ISarosSessionListener sarosSessionListener = new AbstractSarosSessionListener() {
        @Override
        public void sessionStarting(ISarosSession newSarosSession) {
            preferences = (newSarosSession.isHost()) ? mucSessionPreferencesNegotiationManager
                .getOwnPreferences() : mucSessionPreferencesNegotiationManager
                .getSessionPreferences();

            Utils.runSafeAsync(log, new Runnable() {
                @Override
                public void run() {
                    synchronized (MUCManagerSingletonWrapperChatView.this) {
                        mucSession = mucManager.connectMUC(preferences);
                    }
                    log.debug("created / joined MUC" + preferences.getService());
                }
            });
        }

        @Override
        public void sessionEnding(ISarosSession oldSarosSession) {
            synchronized (MUCManagerSingletonWrapperChatView.this) {
                if (mucSession == null)
                    return;

                mucSession.disconnect();
                mucSession = null;
            }
            log.debug("left / destroyed MUC " + preferences.getService());
        }
    };

    public MUCManagerSingletonWrapperChatView(MUCManager mucManager,
        ISarosSessionManager sarosSessionManager) {
        super(mucManager);
        this.sarosSessionManager = sarosSessionManager;
        this.sarosSessionManager.addSarosSessionListener(sarosSessionListener);
    }

    @Override
    public MUCSessionPreferences getPreferences() {
        return preferences;
    }
}
