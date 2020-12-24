package com.example.application.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.application.entity.Menu;
import com.example.application.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@Route(value = "mainview")
@RouteAlias(value = "")
@PWA(name = "springboot-vaadin", shortName = "springboot-vaadin", enableInstallPrompt = true)
public class MainView extends AppLayout {

    private final VerticalLayout menu;
    private H1 viewTitle;

    User user = (User) VaadinSession.getCurrent().getAttribute("user");

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        this.menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        Image img = new Image("images/user.svg", "springboot-vaadin logo");
        img.setTitle(user.getUsername());
        ContextMenu contextMenu = new ContextMenu(img);
        contextMenu.addItem("Logout", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
        });
        contextMenu.setOpenOnClick(true);
        layout.add(img);
        return layout;
    }

    private Component createDrawerContent(VerticalLayout menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "springboot-vaadin logo"));
        logoLayout.add(new H1("Menu"));
        layout.add(logoLayout, menu);
        return layout;
    }

    // private Tabs createMenu() {
    // final Tabs tabs = new Tabs();
    // tabs.setOrientation(Tabs.Orientation.VERTICAL);
    // tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
    // tabs.setId("tabs");
    // tabs.add(createMenuItems());

    // user.getRoles().stream().forEach(role -> {
    // role.getMenus().stream().collect(Collectors.toList()).stream().forEach(menu
    // -> {
    // tabs.getChildren().forEach(tab -> {
    // if (tab.getId().get().equalsIgnoreCase(menu.getMenuName()))
    // tab.setVisible(true);
    // });
    // });
    // });
    // return tabs;
    // }

    private VerticalLayout createMenu() {
        VerticalLayout verticalLayout = new VerticalLayout();
        List<Menu> menus = new ArrayList<>();
        user.getRoles().stream().forEach(role -> {
            role.getMenus().stream().filter(menu -> menu.getIsModul() == true).collect(Collectors.toList()).stream()
                    .forEach(menu -> {
                        // String stringHref = "<a router-link href='" + menu.getPath() + "'>" +
                        // menu.getMenuName() + "</a>";
                        // Html link = new Html(stringHref);
                        // verticalLayout.add(link);
                        menus.add(menu);
                    });
        });
        List<Menu> distinctMenu = menus.stream().distinct().collect(Collectors.toList());
        for (Menu menu : distinctMenu) {
            String stringHref = "<a router-link href='" + menu.getPath() + "'>" + menu.getMenuName() + "</a>";
            Html link = new Html(stringHref);
            verticalLayout.add(link);
        }
        return verticalLayout;
    }

    // private Component[] createMenuItems() {
    // return new Tab[] { createTab("Hello World", HelloWorldView.class),
    // createTab("About", AboutView.class),
    // createTab("Student", Pegawai.class), createTab("Admin", AdminPage.class),
    // createTab("User", UserPage.class) };
    // }

    // private static Tab createTab(String text, Class<? extends Component>
    // navigationTarget) {
    // final Tab tab = new Tab();
    // tab.add(new RouterLink(text, navigationTarget));
    // tab.setId(navigationTarget.getRo);
    // ComponentUtil.setData(tab, Class.class, navigationTarget);
    // tab.setVisible(false);
    // return tab;
    // }

    // @Override
    // protected void afterNavigation() {
    // super.afterNavigation();
    // getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    // viewTitle.setText(getCurrentPageTitle());
    // }

    // private Optional<Tab> getTabForComponent(Component component) {
    // return menu.getChildren().filter(tab -> ComponentUtil.getData(tab,
    // Class.class).equals(component.getClass()))
    // .findFirst().map(Tab.class::cast);
    // }

    // private String getCurrentPageTitle() {
    // return getContent().getClass().getAnnotation(PageTitle.class).value();
    // }
}
