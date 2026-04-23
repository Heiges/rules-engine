import { useParams, useNavigate } from 'react-router-dom'
import './DetailView.css'

const tileNames: Record<string, string> = {
  attributes: 'Attribute',
}

export function DetailView() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const name = id ? (tileNames[id] ?? id) : ''

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/')}>← Zurück</button>
      <h1>{name}</h1>
    </div>
  )
}
