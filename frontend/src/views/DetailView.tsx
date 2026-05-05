import { useParams } from 'react-router-dom'
import './DetailView.css'

const tileNames: Record<string, string> = {
  attributes: 'Attribute',
}

export function DetailView() {
  const { id } = useParams<{ id: string }>()
  const name = id ? (tileNames[id] ?? id) : ''

  return (
    <div className="detail-view">
      <h1>{name}</h1>
    </div>
  )
}
