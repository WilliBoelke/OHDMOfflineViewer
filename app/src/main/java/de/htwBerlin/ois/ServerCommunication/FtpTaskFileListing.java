package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RemoteFile;


/**
 * Async task that lists files hosted on FTP Remote Server
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FtpTaskFileListing extends AsyncTask<Void, Void, String>
{


    //------------Instance Variables------------

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    /**
     * Log Tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * The list to be filled with remote ohdmFiles
     */
    private ArrayList<RemoteFile> remoteFiles;
    /**
     * Implementation of the {@link  AsyncResponse} interface
     * (To be implemented when initializing this class)
     */
    private AsyncResponse delegate;
    /**
     * Context
     */
    private WeakReference<Context> context;
    private boolean includeSubDirs;
    /**
     * The path to the directory
     */
    private String path;

    private SftpClient ftpClient;


    //------------Constructors------------

    /**
     * Public Constructor
     *
     * @param context
     * @param path
     * @param asyncResponse
     */
    public FtpTaskFileListing(Context context, String path, boolean includeSubDirs, AsyncResponse asyncResponse)
    {
        Log.d(TAG, "Constructor : new FtpTaskFileListing with : path  = " + path + " includeSubDirs = " + includeSubDirs);
        this.includeSubDirs = includeSubDirs;
        this.delegate = asyncResponse;
        this.path = path;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------


    @Override
    protected String doInBackground(Void... params)
    {
        remoteFiles = new ArrayList<>();
        Log.d(TAG, "doingInBackground : initializing new FtpClient ");

            ftpClient = new SftpClient();

        ftpClient.connect();
        Log.d(TAG, "doingInBackground : connected to FtpClient");

        RemoteFile[] files = new RemoteFile[0];
        try

        {
            if (includeSubDirs == true)
            {
                Log.d(TAG, "doingInBackground : getting all files including sub dirs...");
                 remoteFiles.addAll(ftpClient.getAllFileList(path));
            }
            else
            {
                Log.d(TAG, "doingInBackground : getting all files...");
                files = ftpClient.getFileList(path);
                for (RemoteFile ftpFile : files)
                {
                 remoteFiles.add(ftpFile);
                }
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "doinIBackground : something went wrong while retrieving files from the FTP Server");
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "doinIBackground : something went wrong while retrieving files from the FTP Server maybe the path wasnt valid?");
            e.printStackTrace();
        }

        Log.d(TAG, "doingInBackground : finished - closing connection : ");
        ftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (remoteFiles.size() == 0)
        {
            Log.e(TAG, "Server not available or empty");
        }
        else
        {
            Log.d(TAG, "Found " + remoteFiles.size() + " files from server");
            delegate.getOhdmFiles(this.remoteFiles);
        }
    }

    public void insertMockFtpClient(FtpClient mockFtpClient)
    {
       // this.ftpClient = mockFtpClient;
    }

    public ArrayList<RemoteFile> getResultList()
    {
        return this.remoteFiles;
    }
}
