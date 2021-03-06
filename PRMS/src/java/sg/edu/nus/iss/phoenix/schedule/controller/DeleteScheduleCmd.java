/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.schedule.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.schedule.delegate.ScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.delegate.ReviewSelectScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

/**
 *
 * @author linby
 */
@Action("deleteSchedule")
public class DeleteScheduleCmd implements Perform {

    @Override
    public String perform(String string, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ScheduleDelegate scheduleDelegate = new ScheduleDelegate();
        String id = req.getParameter("id");
        try {
            scheduleDelegate.processDeleteProgramSlot(Integer.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Fatal Exception:"+e.getMessage());
            return "/pages/error.jsp";
        }

        ReviewSelectScheduleDelegate reviewSelectScheduleDelegate = new ReviewSelectScheduleDelegate();
        try {
            List<ProgramSlot> data = reviewSelectScheduleDelegate.reviewSelectProgramSlot();
            req.setAttribute("scheduleList", data);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());
            return "/pages/crudschedule.jsp";
        }

        return "/pages/crudschedule.jsp";
    }

}
