package de.fu_berlin.inf.dpp.stf.server.rmi.superbot.component.menubar.menu.submenu;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.fu_berlin.inf.dpp.feedback.FeedbackManager;
import de.fu_berlin.inf.dpp.net.JID;

public interface ISarosPreferences extends Remote {

    /**********************************************
     * setting for Account.
     * 
     **********************************************/

    /**
     * Creates an account with GUI, which should be done with the following
     * steps:
     * <ol>
     * <li>Click menu "Saros" -> "Preferences"</li>
     * <li>In the right saros-page click Button "Add Account"</li>
     * <li>in the popup window click button "Create New Account"</li>
     * </ol>
     * 
     * @param jid
     * 
     * @param password
     *            the password of the new account.
     * @throws RemoteException
     */
    public void createAccount(JID jid, String password) throws RemoteException;

    /**
     * add an account in the XMPP-Accounts list with GUI, which should be done
     * with the following steps:
     * <ol>
     * <li>Click menu "Saros" -> "Preferences"</li>
     * <li>In the right saros-page click Button "Add Account"</li>
     * <li>Confirm the popup window with the given parameters</li>
     * </ol>
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * @param password
     *            the password of the added account.
     * @throws RemoteException
     */
    public void addAccount(JID jid, String password) throws RemoteException;

    /**
     * activate the account specified by the given jid using GUI,which should be
     * done with the following steps:
     * <ol>
     * <li>Click menu "Saros" -> "Preferences"</li>
     * <li>In the right saros-page click Button "Activate Account"</li>
     * </ol>
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * 
     * @throws RemoteException
     */
    public void activateAccount(JID jid) throws RemoteException;

    /**
     * change the account specified by the given jid with GUI, which should be
     * done with the following steps:
     * <ol>
     * <li>Click menu "Saros" -> "Preferences"</li>
     * <li>In the right saros-page click Button "Edit Account"</li>
     * <li>confirm the popupwindow with the passed parameters</li>
     * </ol>
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * 
     * @param newPassword
     *            the new password
     * 
     * @throws RemoteException
     */
    public void editAccount(JID jid, String newXmppJabberID, String newPassword)
        throws RemoteException;

    /**
     * delete the account specified by the given jid with GUI, which should be
     * done with the following steps:
     * <ol>
     * <li>Click menu "Saros" -> "Preferences"</li>
     * <li>In the right saros-page click Button "Delete Account"</li>
     * </ol>
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * @throws RemoteException
     */
    public void removeAccount(JID jid) throws RemoteException;

    public void removeAllNonActiveAccounts() throws RemoteException;

    /**********************************************
     * 
     * setting for Feedback
     * 
     **********************************************/

    /**
     * Set feeback disabled using GUI.<br/>
     * To simplify Testing you can disable the automatic reminder, so that you
     * will never get the feedback popup window.
     * <ol>
     * <li>open Preferences dialog by clicking main menu Saros -> preferences</li>
     * <li>click the treenodes: Saros-> Feedback</li>
     * <li>then select the radio button "Disable automatic reminder" in the
     * right page</li>
     * </ol>
     * 
     * @throws RemoteException
     */
    public void disableAutomaticReminder() throws RemoteException;

    /**
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * @return <tt>true</tt> if the red label with the text
     *         "active: jid.getBase()" is visible in the saros-preferences-page.
     * @throws RemoteException
     */
    public boolean isAccountActive(JID jid) throws RemoteException;

    /**
     * 
     * @return the {@link JID} of the current active account or <tt>null</tt> if
     *         there is not account activated
     * @throws RemoteException
     */
    public JID getActiveAccount() throws RemoteException;

    public boolean existsAccount() throws RemoteException;

    /**
     * 
     * 
     * @param jid
     *            a JID which is used to identify the users of the Jabber
     *            network, more about it please see {@link JID}.
     * @return <tt>true</tt> if the account specified by the given parameters
     *         exists in the XMPP-Accounts list on the right
     *         saros-preferences-page.
     * @throws RemoteException
     */
    public boolean existsAccount(JID jid) throws RemoteException;

    /**
     * Set feeback disabled without GUI.<br/>
     * To simplify Testing you can disable the automatic reminder, so that you
     * will never get the feedback popup window.
     * 
     * @see FeedbackManager#setFeedbackDisabled(boolean)
     * 
     * @throws RemoteException
     */
    public void disableAutomaticReminderNoGUI() throws RemoteException;

    /**
     * Checks the button Force XMPP transfer over IBB in the Saros advances
     * preferences. You have to reconnect to the server to apply this change.
     * 
     * @throws RemoteException
     */
    public void enableIBBOnlyTransfer() throws RemoteException;

    /**
     * Unchecks the button Force XMPP transfer over IBB in the Saros advances
     * preferences. You have to reconnect to the server to apply this change.
     * 
     * @throws RemoteException
     */
    public void disableIBBOnlyTransfer() throws RemoteException;

    public void restoreDefaults() throws RemoteException;
}
