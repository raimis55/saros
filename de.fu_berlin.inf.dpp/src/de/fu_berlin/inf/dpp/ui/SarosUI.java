/*
 * DPP - Serious Distributed Pair Programming
 * (c) Freie Universität Berlin - Fachbereich Mathematik und Informatik - 2006
 * (c) Riad Djemili - 2006
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 1, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package de.fu_berlin.inf.dpp.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CancellationException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.session.ISarosSession;
import de.fu_berlin.inf.dpp.session.User;
import de.fu_berlin.inf.dpp.session.User.Permission;
import de.fu_berlin.inf.dpp.ui.util.SWTUtils;
import de.fu_berlin.inf.dpp.ui.views.SarosView;

/**
 * Some helper functionality to interface with Eclipse.
 */
@Component(module = "ui")
public class SarosUI {

    private static final Logger log = Logger.getLogger(SarosUI.class);

    public SarosUI() {

    }

    /**
     * @swt
     */
    public void openSarosView() {
        createView(SarosView.ID);
        activateSarosView();
    }

    /**
     * @swt
     */
    public void activateSarosView() {
        activateView(SarosView.ID);
    }

    protected void bringToFrontView(String view) {
        showView(view, IWorkbenchPage.VIEW_VISIBLE);
    }

    protected void activateView(String view) {
        showView(view, IWorkbenchPage.VIEW_ACTIVATE);
    }

    protected void createView(String view) {
        showView(view, IWorkbenchPage.VIEW_CREATE);
    }

    /**
     * TODO What to do if no WorkbenchWindows are are active?
     */
    protected void showView(String view, int mode) {
        try {
            IWorkbench workbench = PlatformUI.getWorkbench();
            if (workbench == null) {
                log.error("Workbench not created when trying to show view!"); //$NON-NLS-1$
                return;
            }

            IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
            if (window == null) {
                log.error("No Active WorkbenchWindow found " //$NON-NLS-1$
                    + "(the platform is shutting down)" //$NON-NLS-1$
                    + " when trying to show view!"); //$NON-NLS-1$
                return;
            }

            window.getActivePage().showView(view, null, mode);
        } catch (PartInitException e) {
            log.error("Could not create View " + view, e); //$NON-NLS-1$
        }
    }

    public static Composite createLabelComposite(Composite parent, String text) {
        Composite composite = new Composite(parent, SWT.NONE);

        FillLayout layout = new FillLayout(SWT.NONE);
        layout.marginHeight = 20;
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setText(text);

        return composite;
    }

    /**
     * @swt
     */

    public void performPermissionChange(final ISarosSession session,
        final User user, final Permission newPermission) {

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(
            SWTUtils.getShell());

        try {
            dialog.run(true, true, new IRunnableWithProgress() {
                @Override
                public void run(final IProgressMonitor monitor) {

                    final SubMonitor progress = SubMonitor.convert(monitor);

                    try {

                        progress.beginTask(Messages.SarosUI_permission_change,
                            IProgressMonitor.UNKNOWN);

                        session.initiatePermissionChange(user, newPermission);
                        // FIXME run this at least 2 times and if this still
                        // does not succeed kick the user
                    } catch (CancellationException e) {
                        log.warn("permission change failed because user did not respond"); //$NON-NLS-1$
                        SWTUtils.runSafeSWTSync(log, new Runnable() {
                            @Override
                            public void run() {
                                MessageDialog.openInformation(
                                    SWTUtils.getShell(),
                                    Messages.SarosUI_permission_canceled,
                                    Messages.SarosUI_permission_canceled_text);
                            }
                        });
                    } catch (InterruptedException e) {
                        log.error("Code not designed to be interruptable", e); //$NON-NLS-1$
                    } finally {
                        progress.done();
                    }
                }
            });
        } catch (InvocationTargetException e) {
            log.error("Internal Error: ", e); //$NON-NLS-1$
            MessageDialog.openError(SWTUtils.getShell(),
                Messages.SarosUI_permission_failed,
                Messages.SarosUI_permission_failed_text);
        } catch (InterruptedException e) {
            log.error("Code not designed to be interruptable", e); //$NON-NLS-1$
        }
    }
}
