package de.fu_berlin.inf.dpp.test.mocks;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import de.fu_berlin.inf.dpp.activities.SPath;
import de.fu_berlin.inf.dpp.activities.SPathDataObject;
import de.fu_berlin.inf.dpp.filesystem.IPathFactory;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.session.ISarosSession;
import de.fu_berlin.inf.dpp.session.User;

/**
 * This class provides several mock-creating methods for various purposes. It
 * makes use of EasyMock and PowerMock.
 */
public class SarosMocks {

    /**
     * Creates a mocked User object with the given JID
     * 
     * @return a replayed EasyMock
     */
    public static User mockUser(JID jid) {
        User user = EasyMock.createMock(User.class);
        EasyMock.expect(user.getJID()).andStubReturn(jid);
        EasyMock.replay(user);
        return user;
    }

    /**
     * Add the information of a user to the given mocked session.
     * 
     * @param mockedSession
     *            The session to that the user will be known after this call.
     *            The session mock is <i>not</i> replayed.
     * @param user
     *            May be a (replayed) mock
     */
    public static void addUserToSession(ISarosSession mockedSession, User user) {
        EasyMock.expect(mockedSession.getUser(user.getJID())).andStubReturn(
            user);
    }

    /**
     * Creates a mocked SPath (including its SPathDataObject "twin").
     * 
     * The method {@link Object#toString()} is <i>not</i> mocked and may be
     * called (e.g. for logging/messages).
     * 
     * @return a non-replayed PowerMock (must be replayed with
     *         {@link PowerMock#replay(Object...)}).
     */
    public static SPath prepareMockSPath() {
        SPath path = PowerMock.createPartialMockForAllMethodsExcept(
            SPath.class, "toString");
        SPathDataObject pathDO = EasyMock.createMock(SPathDataObject.class);

        EasyMock.expect(
            path.toSPathDataObject(EasyMock.isA(ISarosSession.class),
                EasyMock.isA(IPathFactory.class))).andStubReturn(pathDO);
        EasyMock.expect(
            pathDO.toSPath(EasyMock.isA(ISarosSession.class),
                EasyMock.isA(IPathFactory.class))).andStubReturn(path);

        EasyMock.replay(pathDO);

        return path;
    }

    /**
     * Creates a mocked SPath (including its SPathDataObject "twin")
     * 
     * @return a replayed PowerMock
     */
    public static SPath mockSPath() {
        SPath path = prepareMockSPath();
        PowerMock.replay(path);

        return path;
    }
}
