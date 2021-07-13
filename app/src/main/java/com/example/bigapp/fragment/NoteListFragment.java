package com.example.bigapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bigapp.R;
import com.example.bigapp.activity.NoteActivity;
import com.example.bigapp.data.Note;
import com.example.bigapp.data.NoteLab;
import com.example.bigapp.layout.TitleBar;
import java.util.List;

import static com.example.bigapp.activity.NoteActivity.NOTE_UUID;


public class NoteListFragment extends Fragment {

    private List<Note> mNoteList;
    private TitleBar mTitleBar;
    private NoteAdapter mNoteAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view); //绑定recyclerView视图
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //设置布局管理
        NoteLab noteLab = NoteLab.newInstance(getActivity()); //创建单例泛型列表数据

        mNoteList = noteLab.getNoteList();   //获取列表数据
        //创建适配器
        mNoteAdapter = new NoteAdapter(mNoteList);
        mRecyclerView.setAdapter(mNoteAdapter);//设置适配器

        //标题栏右上角的 + 号
        mTitleBar = view.findViewById(R.id.title_bar_note);
        mTitleBar.setRightIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        NoteLab noteLab = NoteLab.newInstance(getActivity());
        List<Note> noteList = noteLab.getNoteList();
        if(mNoteAdapter == null){
            mNoteAdapter = new NoteAdapter(noteList);
            mRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNoteList(noteList);
            mNoteAdapter.notifyDataSetChanged();
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Note> mNoteList;

        public NoteAdapter(List<Note> noteList) {
            mNoteList = noteList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Note note = mNoteList.get(position);
            holder.bind(note);
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
        public void setNoteList(List<Note> noteList){
            mNoteList = noteList;
        }
    }

    private  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Note mNote;
        TextView mTitleView;
        TextView mTimeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.title_item);
            mTimeView = itemView.findViewById(R.id.time_item);
            itemView.setOnClickListener(this);
        }

        public void bind(Note note) {
            mNote = note;
            mTitleView.setText(note.getTitle());
            mTimeView.setText(note.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }
    }
    public void addNote(){
        Note note = new Note();
        Log.d("Memory", "note ddd" + note.getId() + "  " + note.getDate() + "  " + note.getTitle() + "  " + note.getContent());
        NoteLab.newInstance(getActivity()).addNote(note);
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NOTE_UUID, note.getId());
        startActivity(intent);
    }

}
