package de.fu_berlin.inf.dpp.activities;

import org.apache.commons.lang.ObjectUtils;

import de.fu_berlin.inf.dpp.filesystem.IFile;
import de.fu_berlin.inf.dpp.filesystem.IFolder;
import de.fu_berlin.inf.dpp.filesystem.IPath;
import de.fu_berlin.inf.dpp.filesystem.IPathFactory;
import de.fu_berlin.inf.dpp.filesystem.IProject;
import de.fu_berlin.inf.dpp.filesystem.IResource;
import de.fu_berlin.inf.dpp.session.ISarosSession;

/**
 * Objects of this class point to a document in the *workspace*, possibly with
 * information about a specific open editor for that document.
 * 
 * An SPath consists of an IProject reference, IPath reference and editorType
 * identifier.
 * 
 * @immutable SPaths are value objects and thus immutable.
 */
public class SPath {

    /**
     * @JTourBusStop 4, Some Basics:
     * 
     *               Individual Eclipse projects use IPaths to identify their
     *               resources. However, because Saros needs to keep track of
     *               resources across multiple projects, it encapsulates IPaths
     *               in an SPath that includes additional identifying
     *               information.
     */

    /**
     * The project relative path of the resource or editor this SPath
     * represents.
     */
    protected IPath projectRelativePath;

    /**
     * The local IProject in which the document is contained which this SPath
     * represents
     */
    protected IProject project;

    /**
     * Type of the editor (plain text, Java, XML, ...).
     * 
     * Maybe <code>null</code>, when the SPath addresses a document and not a
     * specific editor.
     * 
     * @TODO Change to something that can be used to identify different editor
     *       types in Eclipse.
     */
    protected String editorType = "txt";

    /**
     * Default constructor, initializing this SPath as a reference to the
     * resource or editor identified by the given path in the given project.
     * 
     * @param path
     *            maybe <code>null</code> to represent "no editor"
     * @throws IllegalArgumentException
     *             if the path is not relative
     */
    public SPath(IProject project, IPath path) {
        if (project == null)
            throw new IllegalArgumentException(
                "SPath must be initialized with an IProject");

        if (path.isAbsolute())
            throw new IllegalArgumentException("path is absolute: " + path);

        this.project = project;
        this.projectRelativePath = path;
    }

    /**
     * Convenience constructor, which retrieves path and project from the given
     * resource
     */
    public SPath(IResource resource) {
        this(resource.getProject(), resource.getProjectRelativePath());
    }

    /**
     * Turns this SPath into an SPathDataObject representing it globally.
     */
    public SPathDataObject toSPathDataObject(ISarosSession sarosSession,
        IPathFactory pathFactory) {

        String id = sarosSession.getProjectID(project);
        if (id == null)
            throw new IllegalArgumentException(
                "Trying to send a SPath which refers to a file in project which is not shared: "
                    + this);

        return new SPathDataObject(id,
            pathFactory.fromPath(projectRelativePath), editorType);
    }

    /**
     * Returns the project relative path of the resource or editor represented
     * by this SPath.
     * 
     * @return May return null if this SPath is used to represent no editor.
     */
    public IPath getProjectRelativePath() {
        return projectRelativePath;
    }

    /**
     * Return the identifier of the editor which this SPath references. This
     * identifier should be used to select one of several editors displaying the
     * same resource.
     * 
     * TODO Make use of this information
     */
    public String getEditorType() {
        return editorType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ObjectUtils.hashCode(editorType);
        result = prime * result + ObjectUtils.hashCode(projectRelativePath);
        result = prime * result + ObjectUtils.hashCode(project);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SPath))
            return false;

        SPath other = (SPath) obj;

        if (!ObjectUtils.equals(this.editorType, other.editorType))
            return false;
        if (!ObjectUtils.equals(this.projectRelativePath,
            other.projectRelativePath))
            return false;
        if (!ObjectUtils.equals(this.project, other.project))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "SPath [editorType="
            + editorType
            + ", path="
            + (projectRelativePath != null ? projectRelativePath
                .toPortableString() : "<no path>") + ", project="
            + (project != null ? project.getName() : "<no project>") + "]";
    }

    /**
     * Returns the IFile represented by this SPath.
     * 
     * @return the IFile contained in the associated IProject for the given
     *         project relative path
     * 
     * @convenience This method is using a straight forward implementation
     */
    public IFile getFile() {
        return project.getFile(projectRelativePath);
    }

    /**
     * Returns the IResource represented by this SPath.
     */
    public IResource getResource() {
        return project.findMember(projectRelativePath);
    }

    /**
     * Returns the IFolder represented by this SPath.
     */
    public IFolder getFolder() {
        return project.getFolder(projectRelativePath);
    }

    /**
     * Returns the project in which the referenced resource or editor is
     * located.
     */
    public IProject getProject() {
        return project;
    }

    /**
     * Convenience method for getting the full (workspace-relative) path of the
     * file/editor identified by this SPath.
     */
    public IPath getFullPath() {
        final IPath fullProjectPath = project.getFullPath();
        return fullProjectPath.append(projectRelativePath);
    }
}
