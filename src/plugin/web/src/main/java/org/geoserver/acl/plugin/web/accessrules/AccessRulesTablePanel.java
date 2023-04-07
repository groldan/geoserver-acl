/* (c) 2023 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.plugin.web.accessrules;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.geoserver.acl.plugin.web.accessrules.model.MutableRule;
import org.geoserver.acl.plugin.web.components.RulesDataProvider;
import org.geoserver.acl.plugin.web.components.RulesTablePanel;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

@SuppressWarnings("serial")
public class AccessRulesTablePanel extends RulesTablePanel<MutableRule> {

    public AccessRulesTablePanel(String id, RulesDataProvider<MutableRule> dataProvider) {
        super(id, dataProvider);
    }

    @Override
    protected ListView<Property<MutableRule>> buildLinksListView(
            final GeoServerDataProvider<MutableRule> dataProvider) {

        RulesDataProvider<MutableRule> provider = (RulesDataProvider<MutableRule>) dataProvider;
        return new TableHeadersView("sortableLinks", provider);
    }

    private class TableHeadersView extends ListView<Property<MutableRule>> {

        public TableHeadersView(String id, RulesDataProvider<MutableRule> provider) {
            super(id, getDataProvider().visibleProperties());
        }

        protected @Override void populateItem(ListItem<Property<MutableRule>> item) {
            Property<MutableRule> property = item.getModelObject();

            // build a sortable link if the property is sortable, a label otherwise
            IModel<String> titleModel = getPropertyTitle(property);

            Component header;
            if (property == PRIORITY) {
                header = priorityHeader(item, titleModel);
            } else if (property == BUTTONS) {
                header = buttonsHeader(item);
            } else {
                header = new Label("header", titleModel);
            }
            item.add(header);
        }

        private Component buttonsHeader(ListItem<Property<MutableRule>> item) {
            ResourceReference imgref =
                    new PackageResourceReference(GeoServerBasePage.class, "img/icons/silk/cog.png");
            // ImageButton btn = new ImageButton("tableSettingsButton", img);
            Image img = new Image("tableSettingsButton", imgref);

            // setImgAlt(tableOptionsLink, "RulesTablePanel.buttons.down");
            Fragment f = new Fragment("header", "buttonsHeader", AccessRulesTablePanel.this);
            f.add(img);
            return f;
        }

        private Component priorityHeader(
                ListItem<Property<MutableRule>> item, IModel<String> titleModel) {
            Fragment f = new Fragment("header", "sortableHeader", AccessRulesTablePanel.this);
            AjaxLink<Property<MutableRule>> link = sortableLink(item);
            link.add(new Label("label", titleModel));
            f.add(link);
            return f;
        }

        /**
         * Builds a sort link that will force sorting on a certain column, and flip it to the other
         * direction when clicked again
         */
        protected <S> AjaxLink<S> sortableLink(ListItem<S> item) {
            return new AjaxLink<S>("link", item.getModel()) {
                @SuppressWarnings("unchecked")
                public @Override void onClick(AjaxRequestTarget target) {
                    RulesDataProvider<MutableRule> dataProvider = getDataProvider();
                    SortParam<?> currSort = dataProvider.getSort();
                    Property<MutableRule> property = (Property<MutableRule>) getModelObject();
                    if (currSort == null || !property.getName().equals(currSort.getProperty())) {
                        dataProvider.setSort(new SortParam<>(property.getName(), true));
                    } else {
                        dataProvider.setSort(
                                new SortParam<>(property.getName(), !currSort.isAscending()));
                    }
                    clearSelection();
                    target.add(listContainer);
                    rememeberSort();
                }
            };
        }

        private void rememeberSort() {
            MetadataMap sorts = getFromSession(SORT_INPUTS, MetadataMap::new);
            RulesDataProvider<MutableRule> dataProvider = getDataProvider();
            sorts.put(dataProvider.getClass().getName(), dataProvider.getSort());
        }
    }
}
