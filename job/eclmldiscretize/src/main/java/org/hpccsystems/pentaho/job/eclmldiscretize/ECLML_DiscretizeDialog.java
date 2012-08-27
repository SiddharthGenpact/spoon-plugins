/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclmldiscretize;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.hpccsystems.ecljobentrybase.*;

/**
 *
 * @author ChambersJ
 */
public class ECLML_DiscretizeDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

    private ECLML_Discretize jobEntry;
    
    private Text jobEntryName;

    private Text recordsetName;
    
    
    private Text recordName;
    private Text cooccur;
    private Combo routine;

    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;

    public ECLML_DiscretizeDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLML_Discretize) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Discretize");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;


 

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Define an ECL ML Discretize");

        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        // Stepname line
        Group generalGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 600;
        generalGroupFormat.height = 75;
        generalGroupFormat.left = new FormAttachment(0, 0);
        generalGroup.setLayoutData(generalGroupFormat);
        
        jobEntryName = buildText("Job Entry Name", null, lsMod, middle, margin, generalGroup);



        Group recordGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(recordGroup);
        recordGroup.setText("Record Details");
        recordGroup.setLayout(groupLayout);
        FormData recordGroupFormat = new FormData();
        recordGroupFormat.top = new FormAttachment(generalGroup, margin);
        recordGroupFormat.width = 600;
        recordGroupFormat.height = 100;
        recordGroupFormat.left = new FormAttachment(0, 0);
        recordGroup.setLayoutData(recordGroupFormat);
        
        //name = buildText("Distribute Name", null, lsMod, middle, margin, distributeGroup);


        recordName = buildText("Record Name", null, lsMod, middle, margin, recordGroup);
           
        cooccur = buildText("Minimum co-occur", recordName, lsMod, middle, margin, recordGroup);
        //routine = buildText("Routine", cooccur, lsMod, middle, margin, recordGroup);
        routine = buildCombo("Routine", cooccur, lsMod, middle, margin, recordGroup, new String[]{"", "ByBucketing", "ByRounding", "ByTiling"});
       
        Group outputGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(outputGroup);
        outputGroup.setText("Output");
        outputGroup.setLayout(groupLayout);
        FormData iterateGroupFormat = new FormData();
        iterateGroupFormat.top = new FormAttachment(recordGroup, margin);
        iterateGroupFormat.width = 600;
        iterateGroupFormat.height = 75;
        iterateGroupFormat.left = new FormAttachment(0, 0);
        outputGroup.setLayoutData(iterateGroupFormat);
        
        
        recordsetName = buildText("Output Recordset", null, lsMod, middle, margin, outputGroup);
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, outputGroup);

        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });
        


        //if (jobEntry.getJobName() != null) {
        //    jobEntryName.setText(jobEntry.getJobName());
        //}
        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }
        

        

        if (jobEntry.getRecordsetName() != null) {
            recordsetName.setText(jobEntry.getRecordsetName());
        }
       
        if (jobEntry.getRecordName() != null) {
            recordName.setText(jobEntry.getRecordName());
        }
        
       
        if (jobEntry.getCooccur() != null) {
            cooccur.setText(jobEntry.getCooccur());
        }
        
        if (jobEntry.getRoutine() != null) {
            routine.setText(jobEntry.getRoutine());
        }



        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }


    private void ok() {
           
        jobEntry.setName(jobEntryName.getText());
        jobEntry.setRecordsetName(recordsetName.getText());
        jobEntry.setRecordName(recordName.getText());
        jobEntry.setCooccur(cooccur.getText());
        jobEntry.setRoutine(routine.getText());

        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }

    
}
