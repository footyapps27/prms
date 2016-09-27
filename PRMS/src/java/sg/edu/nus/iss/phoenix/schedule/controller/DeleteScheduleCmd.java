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
@Action("deleteschedule")
public class DeleteScheduleCmd implements Perform {

    @Override
    public String perform(String string, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ScheduleDelegate del = new ScheduleDelegate();
        ProgramSlot psEntity = new ProgramSlot();
        String id = req.getParameter("id");
        psEntity.setId(Integer.valueOf(id));
        del.processDeleteProgramSlot(psEntity);
        ReviewSelectScheduleDelegate rssdel = new ReviewSelectScheduleDelegate();
        List<ProgramSlot> list = rssdel.reviewSelectProgramSlot();
        req.setAttribute("schedulelist", list);
        return "/pages/crudschedule.jsp";
    }

}
