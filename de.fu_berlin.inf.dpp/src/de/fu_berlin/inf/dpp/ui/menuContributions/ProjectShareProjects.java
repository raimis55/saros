package de.fu_berlin.inf.dpp.ui.menuContributions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jivesoftware.smack.RosterEntry;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.Saros;
import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.ui.Messages;
import de.fu_berlin.inf.dpp.ui.util.CollaborationUtils;
import de.fu_berlin.inf.dpp.ui.util.selection.retriever.SelectionRetrieverFactory;

/**
 * This class fills a {@link Menu} with {@link MenuItem}s.<br/>
 * Each {@link MenuItem} represents an {@link IProject}.<br/>
 * A click leads to a shared project invitation.
 */
public class ProjectShareProjects extends ContributionItem {

    @Inject
    protected Saros saros;

    protected WorkbenchLabelProvider workbenchLabelProvider = new WorkbenchLabelProvider();

    public ProjectShareProjects() {
        this(null);
    }

    public ProjectShareProjects(String id) {
        super(id);
        SarosPluginContext.initComponent(this);
    }

    @Override
    public void fill(Menu menu, int index) {
        final List<JID> selectedBuddies = SelectionRetrieverFactory
            .getSelectionRetriever(JID.class).getSelection();

        IProject[] workspaceProjects = getSortedWorkspaceProjects();
        if (workspaceProjects.length > 0) {
            for (int i = 0; i < workspaceProjects.length; i++) {
                IProject project = workspaceProjects[i];
                createProjectMenuItem(menu, i, project, selectedBuddies);
            }
        } else {
            createNoProjectsMenuItem(menu, 0);
        }
    }

    /**
     * Returns a sorted list of all {@link IProject}s in the {@link IWorkspace}.
     * 
     * @return
     */
    public IProject[] getSortedWorkspaceProjects() {
        IProject[] workspaceProjects = ResourcesPlugin.getWorkspace().getRoot()
            .getProjects();
        Arrays.sort(workspaceProjects, new Comparator<IProject>() {
            @Override
            public int compare(IProject o1, IProject o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                return name1.compareToIgnoreCase(name2);
            }
        });
        return workspaceProjects;
    }

    /**
     * Creates a menu entry which shares projects with the given
     * {@link RosterEntry}.
     * 
     * @param parentMenu
     * @param index
     * @param project
     * @param selectedContacts
     * @return
     */
    protected MenuItem createProjectMenuItem(Menu parentMenu, int index,
        final IProject project, final List<JID> selectedContacts) {

        MenuItem menuItem = new MenuItem(parentMenu, SWT.NONE, index);
        menuItem.setText(workbenchLabelProvider.getText(project));
        menuItem.setImage(workbenchLabelProvider.getImage(project));

        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                List<IResource> resources = new ArrayList<IResource>();
                resources.add(project);
                CollaborationUtils.startSession(resources, selectedContacts);
            }
        });

        return menuItem;
    }

    /**
     * Creates a menu entry which indicates that no Saros enabled buddies are
     * online.
     * 
     * @param parentMenu
     * @param index
     * @return
     */
    protected MenuItem createNoProjectsMenuItem(Menu parentMenu, int index) {
        MenuItem menuItem = new MenuItem(parentMenu, SWT.NONE, index);
        menuItem
            .setText(Messages.ProjectShareProjects_no_projects_in_workspace);
        menuItem.setEnabled(false);
        return menuItem;
    }

}
