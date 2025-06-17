package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import frames.GDrawingPanel;
import global.GConstants.EFileMenuItem;


public class GFileMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    
    private File dir;
    private File file;

    private GDrawingPanel drawingPanel;
    @SuppressWarnings("unused")
	private JMenu decorateMenu;
    @SuppressWarnings("unused")
	private JMenuItem decorateShapeItem;

    public GFileMenu() {
        super("File");

        ActionHandler actionhandler = new ActionHandler();

        for (EFileMenuItem menuItem : EFileMenuItem.values()) {
            JMenuItem jMenuItem = new JMenuItem(menuItem.getName());
            jMenuItem.addActionListener(actionhandler);
            jMenuItem.setActionCommand(menuItem.name());
            this.add(jMenuItem);
        }
    }
    
    
    public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;		
	}

    public void initialize() {
		this.dir = new File("/Users/baehanjun/Desktop");
		this.file = null;
	}

    public void newPanel() {
		System.out.println("newPanel");
		if (!this.close()) {
			// new
			this.drawingPanel.initialize();
		}
	}

    public void open() {
        System.out.println("open");
        if (this.file == null) {
            System.out.println("파일이 지정되지 않았습니다.");
            // 또는 사용자에게 알림
            javax.swing.JOptionPane.showMessageDialog(null, "파일이 지정되지 않았습니다.");
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
            this.drawingPanel.setShapes(objectInputStream.readObject());
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
		if (this.file == null) {
			if (!this.saveAs()) {
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(this.file);
					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
					objectOutputStream.writeObject(this.drawingPanel.getShapes());
					objectOutputStream.close();
					this.drawingPanel.setBUpdated(false);
				} catch (IOException e) {
					e.printStackTrace();
				}		

			}
		}
	}

    public boolean saveAs() {
		boolean bCancel = false;		
		JFileChooser chooser = new JFileChooser(this.dir);
		chooser.setSelectedFile(this.file);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Graphics Data", "gvs");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this.drawingPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.dir = chooser.getCurrentDirectory();
			this.file = chooser.getSelectedFile();
		} else {
			bCancel = true;
		}		
		return bCancel;	
	}

    public void print() {
        System.out.println("print");
    }
    
    public boolean close() {
		boolean bCancel = false;
		if (this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.NO_OPTION;
			reply = JOptionPane.showConfirmDialog(this.drawingPanel, "변경 내용을 저장할까요?");
			if (reply == JOptionPane.CANCEL_OPTION) {	
				bCancel = true;
			} else if (reply == JOptionPane.OK_OPTION) {
//				bCancel = this.save();
				this.save();					
			}
		}
		return bCancel;
	}

    public void quit() {
		if (!this.close()) {
			// quit
			System.exit(0);
		}
	}
    


    private void invokeMethod(String methodName) {
		try {
			this.getClass().getMethod(methodName).invoke(this);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException exception) {
			exception.printStackTrace();
		}		
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			EFileMenuItem eFileMenuItem = EFileMenuItem.valueOf(event.getActionCommand());
			invokeMethod(eFileMenuItem.getMethodName());
		}		
	}
}