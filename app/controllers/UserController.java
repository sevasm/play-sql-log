package controllers;

import javax.inject.Inject;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.index;
import views.html.users;

public class UserController extends Controller {
    private final UserService userService;

    private final Form<User> userForm;

    @Inject
    public UserController(UserService userService, FormFactory formFactory) {
        this.userService = userService;

        this.userForm = formFactory.form(User.class);
    }

    public Result index() {
        return ok(index.render());
    }

    @Transactional(readOnly = true)
    public Result getUsers(boolean useJdbc) {
        return ok(users.render(userService.getUsers(useJdbc), userForm, useJdbc));
    }

    @Transactional
    public Result addUser(boolean useJdbc) {
        Form<User> form = userForm.bindFromRequest("username");
        if (form.hasErrors()) {
            return badRequest(users.render(userService.getUsers(useJdbc), form, useJdbc));
        }

        userService.insertUser(form.get(), useJdbc);

        return redirect(routes.UserController.getUsers(useJdbc));
    }
}
