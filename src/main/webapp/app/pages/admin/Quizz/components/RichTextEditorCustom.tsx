import { RichTextEditor, Link } from '@mantine/tiptap';
import { useEditor } from '@tiptap/react';
import Highlight from '@tiptap/extension-highlight';
import StarterKit from '@tiptap/starter-kit';
import Underline from '@tiptap/extension-underline';
import TextAlign from '@tiptap/extension-text-align';
import Superscript from '@tiptap/extension-superscript';
import SubScript from '@tiptap/extension-subscript';
import React from 'react';

const bd = '0.5px solid #E5E7EB';

export const RichTextEditorCustom = () => {
  const editor = useEditor({
    extensions: [StarterKit, Underline, Link, Superscript, SubScript, Highlight, TextAlign.configure({ types: ['heading', 'paragraph'] })],
  });

  return (
    <RichTextEditor editor={editor}>
      <RichTextEditor.Toolbar sticky stickyOffset={60}>
        <RichTextEditor.ControlsGroup>
          <RichTextEditor.Bold bd={bd} />
          <RichTextEditor.Italic bd={bd} />
          <RichTextEditor.Underline bd={bd} />
          <RichTextEditor.Strikethrough bd={bd} />
        </RichTextEditor.ControlsGroup>
        <RichTextEditor.ControlsGroup>
          <RichTextEditor.H1 bd={bd} />
          <RichTextEditor.H2 bd={bd} />
          <RichTextEditor.H3 bd={bd} />
          <RichTextEditor.H4 bd={bd} />
        </RichTextEditor.ControlsGroup>
        <RichTextEditor.ControlsGroup>
          <RichTextEditor.Hr bd={bd} />
          <RichTextEditor.BulletList bd={bd} />
          <RichTextEditor.OrderedList bd={bd} />
        </RichTextEditor.ControlsGroup>
        <RichTextEditor.ControlsGroup>
          <RichTextEditor.AlignLeft bd={bd} />
          <RichTextEditor.AlignCenter bd={bd} />
          <RichTextEditor.AlignJustify bd={bd} />
          <RichTextEditor.AlignRight bd={bd} />
        </RichTextEditor.ControlsGroup>
        <RichTextEditor.ControlsGroup>
          <RichTextEditor.Undo bd={bd} />
          <RichTextEditor.Redo bd={bd} />
        </RichTextEditor.ControlsGroup>
      </RichTextEditor.Toolbar>
      <RichTextEditor.Content />
    </RichTextEditor>
  );
};
