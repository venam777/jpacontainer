/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vaadin.addon.jpacontainer.filter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.AbstractJunctionFilter;

/**
 * Пример:<br/>
 * Запрос на SQL: select * from sysuser u where u.user_locked=0 and
 * exists (select id from right_to_project rtp  where rtp.sysuser_id = u.id and rtp.project_id = 2 and rtp.role_id = 1)
 * and exists (select id from right_to_project rtp  where rtp.sysuser_id = u.id and rtp.project_id = 2 and (rtp.role_id = 6 or rtp.role_id = 7)) order by username <br/>
 * Фильтр будет такой: and(eq("disabled", 0), exists(RightToProject.class, "sysUser", and(eq("project", 2), eq("role", 1)) ), exists(RightToProject.class, "sysUser", and( eq("project", 2), or( eq("role", 6), eq("role", 7)))))
 */
public class ExistFilter extends AbstractJunctionFilter {

    protected Class domainClass;
    protected String propertyName;

    /**
     *
     * @param domainClass Класс домена, из таблицы которого будет выполняться подзапрос exists
     * @param propertyName название свойства, по которому будут соединяться основной запрос и подзапрос (или null, если связь с основным запросом не нужна)
     * @param filters фильтры для подзапроса
     */
    public ExistFilter(Class domainClass, String propertyName, Container.Filter... filters) {
        super(filters);
        this.domainClass = domainClass;
        this.propertyName = propertyName;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        for (Container.Filter f : getFilters()) {
            if (!f.passesFilter(itemId, item)) {
                return false;
            }
        }
        return true;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class domainClass) {
        this.domainClass = domainClass;
    }
}
