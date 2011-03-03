package de.fu_berlin.inf.dpp.ui.widgetGallery.suits.basic.illustrated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.fu_berlin.inf.dpp.ui.widgetGallery.demos.DemoContainer;
import de.fu_berlin.inf.dpp.ui.widgetGallery.demos.DescriptiveDemo;
import de.fu_berlin.inf.dpp.ui.widgets.IllustratedComposite;
import de.fu_berlin.inf.dpp.ui.widgets.SimpleIllustratedComposite;
import de.fu_berlin.inf.dpp.ui.widgets.SimpleIllustratedComposite.IllustratedText;

public class SimpleIllustratedCompositeDemo extends DescriptiveDemo {
	public SimpleIllustratedCompositeDemo(DemoContainer demoContainer,
			String title) {
		super(demoContainer, title);
	}

	@Override
	public String getDescription() {
		return null;
	}

	protected IllustratedComposite createIllustratedComposite(Composite parent,
			int iconId, int position, String text) {
		IllustratedText content = new IllustratedText(iconId, text);

		SimpleIllustratedComposite illustratedComposite = new SimpleIllustratedComposite(
				parent, SWT.NONE | position);
		illustratedComposite.setContent(content);

		return illustratedComposite;
	}

	@Override
	public void createContent(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		int[] iconIds = new int[] { SWT.DEFAULT, SWT.ICON_CANCEL,
				SWT.ICON_ERROR, SWT.ICON_INFORMATION, SWT.ICON_QUESTION,
				SWT.ICON_SEARCH, SWT.ICON_WARNING, SWT.ICON_WORKING };
		int[] positions = new int[] { SWT.NONE, SWT.TOP, SWT.TOP, SWT.BOTTOM,
				SWT.BOTTOM, SWT.CENTER, SWT.CENTER, SWT.CENTER };
		String[] texts = new String[] { "SWT.BORDER\nSWT.DEFAULT\nSWT.DEFAULT",
				"SWT.BORDER\nSWT.ICON_CANCEL\nSWT.TOP",
				"SWT.BORDER\nSWT.ICON_ERROR\nSWT.TOP",
				"SWT.BORDER\nSWT.ICON_INFORMATION\nSWT.BOTTOM",
				"SWT.BORDER\nSWT.ICON_QUESTION\nSWT.BOTTOM",
				"SWT.BORDER\nSWT.ICON_SEARCH\nSWT.CENTER",
				"SWT.BORDER\nSWT.ICON_WARNING\nSWT.CENTER",
				"SWT.BORDER\nSWT.ICON_WORKING\nSWT.CENTER" };

		for (int i = 0; i < iconIds.length; i++) {
			Label label = new Label(parent, SWT.NONE);
			label.setText(texts[i]
					+ ((i == iconIds.length - 1) ? "\nGridData.FILL" : ""));
			label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
					false));

			String text = "I'm a "
					+ SimpleIllustratedComposite.class.getSimpleName()
					+ " instance.\n...\n...";
			IllustratedComposite illustratedComposite = createIllustratedComposite(
					parent, iconIds[i], positions[i], text);
			illustratedComposite
					.setLayoutData((i == iconIds.length - 1) ? new GridData(
							SWT.FILL, SWT.FILL, true, true) : new GridData(
							SWT.FILL, SWT.CENTER, true, false));
		}
	}
}
